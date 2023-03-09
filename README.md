# spring-security-the-good-parts-code

Code for "Spring Security: The Good Parts"

## Demo scenario


### Step 1: A Boot app, with no security

We create a `/public` and `/private` page. No security is applied.

The `/private` page has some tricks to show a logout button ... if some `_csrf` token is available.
It's not the case yet, it will be injected by Spring Security later on. When you think about it, it
makes sense: you can't "log in", so you shouldn't be able to "log out" either.

### Step 2: Basic Spring-Security setup

We introduce Spring Security. We create a `SecurityConfig` configuration class, where we will store
all of our security-related configuration customizations.

The first customization is to create a custom SecurityFilterChain, to override the defaults that
Spring Boot give us. We make sure that every request must be authenticated, except the few things
that we deem "public", such as `/` (public page), the favicon, the css resources, etc. We also
introduce `.formLogin()` to ensure form login is enabled for us.

We also introduce our own UserDetailsService, because we don't want to have to copy the random
password that Boot generates every time we boot up the app. It's an in-memory implementation, for
demo purposes.

Finally, we leverage the `Authentication` object that Spring Security creates for us to display the
name of the logged-in user.

### Step 3: Add SSO, with oauth2-login

We add the dependencies and the code to make SSO login work. It requires adding some properties, and
just enabling "oauth2 login" in our previous security configuration. Everything stays the same.

We want a nicer "authentication name" when logged in with Google, rather than the ID we get back, so
we tweak our GreetingController to display the e-mail when doing SSO login.

### Step 4: Our first Filter

We create our first Filter, the `VerbotenFilter`. It sets the response code to 403 (Forbidden), with
an error message, when the `x-forbidden: true` header is present. Otherwise it's a no-op.

We register it in our filter chain.

You can see the results, e.g. using HTTPie or cURL:

```shell
$ curl localhost:8080 -H "x-forbidden: true" -v
$ # or
$ http localhost:8080 x-forbidden:true
```

### Step 5: RobotAuthenticationFilter

We create a filter that authenticates the user. In this case, it's a "robot account" that sends us a
secret password in the `X-Robot-Password` header.

If the header is present, we make sure the password is correct. If the password is incorrect, we
reject the request. If the password is fine, we perform the actual authentication by creating an
instance of an appropriate implementation of the `Authentication` interface.

If the header is absent, we let the rest of the filter chain decide what to do.

See:

```shell
$ curl localhost:8080/private -H "x-robot-password: beep-boop"
$ # or
$ http localhost:8080/private x-robot-password:beep-boop
```

### Step 6: Our first AuthenticationProvider, because user "daniel" is _special_

We create an AuthenticationProvider that will let user "daniel" in, no matter the password.

We also turn on HTTP Basic authentication - and we don't have to do anything to grant the same
rights to "daniel".

See:

```shell
$ curl localhost:8080/private -u "daniel:foobar"
$ # or
$ http localhost:8080/private --auth "daniel:barfoo"
```

This leverages the `ProviderManager`, which, for examples, produces Spring events when a login
succeeds or fails. We listen to `AuthenticationSuccessEvents` and do a System.out.println with the
Authentication class and the Authentication name. Notice how RobotAuthentications are _not_ logged,
as they happen directly in a filter, without using an AuthenticationManager.

### Step 7: Rework the RobotAuthenticationFilter to use AuthenticationManager

We make sure the RobotAuthenticationFilter actually uses an Authentication Manager, like the rest of
Spring Security. To do this, we need to create an "unauthenticated" version of the
RobotAuthentication. We also need an AuthenticationProvider to handle those RobotAuthentication.

To get an AuthenticationManager, we access the Filter chain's local AuthenticationManager. See
the [Spring Security without the WebSecurityConfigurerAdapter blog post](https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter),
which talks about how to access that class. We create a bespoke AbstractHttpConfigurer and put all
Robot-related configuration in the class, so we can get the authentication manager.

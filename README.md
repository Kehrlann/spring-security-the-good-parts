# spring-security-the-good-parts-code

Code for "Spring Security: The Good Parts"

## Demo scenario


### Step 1: A Boot app, with no security

We create a `/public` and `/private` page. No security is applied.

The `/private` page has some tricks to show a logout button ... if some `_csrf` token is available.
It's not the case yet, it will be injected by Spring Security later on. When you think about it, it
makes sense: you can't "log in", so you shouldn't be able to "log out" either.

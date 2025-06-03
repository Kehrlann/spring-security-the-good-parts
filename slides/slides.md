---
theme: default
class: "text-center"
highlighter: prism
lineNumbers: true
transition: none
# use UnoCSS
css: unocss
aspectRatio: "16/9"
colorSchema: "light"
canvasWidth: 1024
---

# **Spring Security:**
## The Good Parts™

<br>
<br>

Daniel Garnier-Moiroux

betterCode() Spring, 2025-05-03


---
layout: image-right
image: /daniel-intro.jpg
hideInToc: true
class: smaller
---

#### Daniel
### Garnier-Moiroux
<br>

Software Engineer @ Broadcom

- <logos-spring-icon /> Spring + Tanzu
- <logos-bluesky /> @garnier.wf
- <logos-firefox /> https://garnier.wf/
- <logos-github-icon /> github.com/Kehrlann/
- <fluent-emoji-flat-envelope-with-arrow /> contact@garnier.wf


---
layout: section
---

# Spring Security

<br>

# <v-click>😬</v-click> <v-click>🤯</v-click> <v-click>🤕</v-click> <v-click>😱</v-click> <v-click>😵‍💫</v-click>

---
layout: default
---

<br />

> ### I have a complex scenario. What could be wrong?
>
> <br />
>
> You need an understanding of the technologies you intend to use before you can successfully build
> applications with them. Security is complicated. Setting up a simple configuration [...] is
> reasonably straightforward. 
>
> <br />
>
> However, **if you try to jump straight to a complicated [configuration], you are almost certain to
> be frustrated**. [...] So you need to take things one step at a time.
>
> <br /> <br />
>
> _source: Spring Security reference docs, FAQ_

---
layout: section
---

# Spring Security

<br>

# 😬 🤯 🤕 😱 😵‍💫

---
layout: section
---

# Spring Security

<br>

# ❤️ ❤️ ❤️ ❤️ ❤️

---

# Contents

1. 🤹️ Demo: a baseline
1. 📚 The theory
    1. 🛂 `Filter` - HTTP building block
    1. 🪪 `Authentication` - the "domain language"
    1. ⚙️ `AuthenticationProvider` - to authenticate
    1. 🧰️ `Configurers` - wiring things together

---

# Contents

1. **🤹️ Demo: a baseline**
1. 📚 The theory
    1. 🛂 `Filter` - HTTP building block
    1. 🪪 `Authentication` - the "domain language"
    1. ⚙️ `AuthenticationProvider` - to authenticate
    1. 🧰️ `Configurers` - wiring things together

---
layout: section
---

# Demo

## 🍃️🔐️ A basic, secured app

---

# Contents

1. 🤹️ Demo: a baseline
1. 📚 The theory
    1. **🛂 `Filter` - HTTP building block**
    1. 🪪 `Authentication` - the "domain language"
    1. ⚙️ `AuthenticationProvider` - to authenticate
    1. 🧰️ `Configurers` - wiring things together

---

# Spring Security Filter

```java
public void doFilter(
  HttpServletRequest request, 
  HttpServletResponse response, 
  FilterChain chain
  ) {
    // 1. Before the request proceeds further (e.g. authentication or reject req)
    // ...

    // 2. Invoke the "rest" of the chain
    chain.doFilter(request, response);

    // 3. Once the request has been fully processed (e.g. cleanup)
    // ...
}
```

---
layout: image
image: security-filter-chain.png
---

---
layout: center
---

<img src="filter-chain-oop.png" style="max-height: 500px;"/>

---
layout: center
---

<img src="filter-chain-call-stack.png" style="max-height: 500px;"/>

---
layout: center
---

<img src="filterchain-callstack-2.jpg" />

---
layout: section
---

# Demo

## ⛔️ Our first filter


---
layout: section
---

## A detailed example

<br>

# `CsrfFilter.java`


---
layout: image-right
image: csrf-exploit.png
backgroundSize: contain
---

# **C**ross
# **S**ite
# **R**equest
# **F**orgery

---
layout: image-right
image: csrf-protection.png
backgroundSize: contain
---

# Protection

<br>

```html
<form ...>
 <!-- visible inputs -->
  <input
      type="hidden"
      name="_csrf"
      value="yyy" />
</form>
```

---
layout: section
---

## A "real" example

<br>

# `CsrfFilter.java`


---

# Other filters?

<br>

<v-click>

Static, on startup: `DefaultSecurityFilterChain`

<br>

Dynamic, at runtime:

```yaml
logging.level:
  org.springframework.security: TRACE
```
</v-click>


---

# Recap

1. Basic interface `Filter`, specifically `OncePerRequestFilter`
    1. Takes HttpServletRequest, HttpServletResponse
    1. Reads from request
        1. Sometimes writes to Response
        1. Sometimes does nothing!
    1. If request is "secure", calls `filterChain.doFilter(...)`
1. Filters are registered `SecurityFilterChain`
    1. Order matters
    1. _Before_ `AuthorizationFilter.class`

---

# Contents

1. 🤹️ Demo: a baseline
1. 📚 The theory
    1. 🛂 `Filter` - HTTP building block
    1. **🪪 `Authentication` - the "domain language"**
    1. ⚙️ `AuthenticationProvider` - to authenticate
    1. 🧰️ `Configurers` - wiring things together

---

# Authentication objects

<br />

Spring Security produces `Authentication` objects. They are used for:

- Authentication (`authn`): _who_ is the user?
- Authorization (`authz`): is the user _allowed to perform_ XYZ?

---

# Vocabulary

<br />

- **Authentication**: represents the user. Contains:
  - **Principal**: user "identity" (name, email...)
  - **GrantedAuthorities**: "permissions" (`roles`, ...)

---

# Vocabulary (cont')

<br />

- **Authentication** also contains:
  - **.isAuthenticated()**: almost always `true`
  - **details**: details about the _request_
  - (Credentials): "password", often `null`

---
layout: image-right
image: security-context.png
backgroundSize: contain
---

# SecurityContext

- Thread-local
- Not propagated to child threads
- Cleared after requests is processed


---
layout: cover
---

# What's the most common `Authentication` implementation?

---

# Good practice

<br>

**DO NOT**

Use `UsernamePasswordAuthenticationToken` everywhere

<br>

**INSTEAD**

Create your own `Authentication` subclasses

---

# Remember our filter?

```java
public void doFilter(
  HttpServletRequest request, 
  HttpServletResponse response, 
  FilterChain chain
  ) {
    // 1. Before the request proceeds further (e.g. authentication or reject req)
    // ...

    // 2. Invoke the "rest" of the chain
    chain.doFilter(request, response);

    // 3. Once the request has been fully processed (e.g. cleanup)
    // ...
}
```

---

# More like this

```java
public void doFilter(
  HttpServletRequest request, 
  HttpServletResponse response, 
  FilterChain chain
  ) {
    // 1. Decide whether the filter should be applied

    // 2. Apply filter: authenticate or reject request

    // 3. Invoke the "rest" of the chain
    chain.doFilter(request, response);

    // 4. No cleanup
}
```

---
layout: section
---

# Demo

## 🤖 Robot wants Auth

---

# Recap

1. Some filters produce an `Authentication`
    1. Read the request ("convert" to domain object)
    1. Authenticate (are the credentials valid?)
    1. Save the `Authentication` in the `SecurityContext`
    1. Or reject the request when creds invalid
1. There's more than just `UsernamePasswordAuthenticationToken`!

---

# Contents

1. 🤹️ Demo: a baseline
1. 📚 The theory
    1. 🛂 `Filter` - HTTP building block
    1. 🪪 `Authentication` - the "domain language"
    1. **⚙️ `AuthenticationProvider` - to authenticate**
    1. 🧰️ `Configurers` - wiring things together

---

# Authentication

Muahaha I lied 😈

<br>

`Authentication` objects are both:
- The result of a _authentication action_
- An _authentication request_

---
layout: center
---

<img src="authentication-manager.png" style="max-height: 400px">

---
layout: center
---

<img src="provider-manager.png" style="max-height: 400px">

---
layout: section
---

# Demo

## 🧑🏻 Daniel's edge-case

---

# Recap

<br>

1. `Authentication` is both an auth request and a successful auth result
1. `AuthenticationProvider` validate credentials
    1. Operates only within the "auth" domain (no HTTP, HTML, ...)
1. `AuthenticationProvider` leverages Spring Security infrastructure


---

# Contents

1. 🤹️ Demo: a baseline
1. 📚 The theory
    1. 🛂 `Filter` - HTTP building block
    1. 🪪 `Authentication` - the "domain language"
    1. ⚙️ `AuthenticationProvider` - to authenticate
    1. **🧰️ `Configurers` - wiring things together**

---

# Wrapping up

<br>

1. `Filter` for security decisions on HTTP requests
1. `Authentication` is the domain language of Spring Security
1. `AuthenticationProvider` to validate credentials
1. `Filter` + `AuthenticationProvider` for custom login

---

# References

### **https://github.com/Kehrlann/spring-security-the-good-parts**

<div style="float:right; margin-right: 00px; text-align: center;">
  <img src="/qr-code.png" style="margin-bottom: -45px; margin-top: 15px;" >
</div>

<br>

- <logos-bluesky /> @garnier.wf
- <logos-firefox /> https://garnier.wf/
- <fluent-emoji-flat-envelope-with-arrow /> contact@garnier.wf

---
layout: image
hideInToc: true
image: /meet-me.jpg
class: end
---

# **😁 Thank you!**

# SPRING ADVANCED
## 최길중(Spring_3기)

## 0. [문제 파악]

![](https://velog.velcdn.com/images/choil8228/post/6bb8a8f1-2cb1-47b0-80bd-2c237e022db5/image.png)

----
필수 0번: application 추가로 이제 실행은 된다.
![](https://velog.velcdn.com/images/choil8228/post/0d0a951a-14e8-4b3e-8da4-04c57e59029d/image.png)

mysql 추가를 안 하고 맨날 실행을 눌러봐서 종종 겪었던 문제.

----
## 1번 [문제]

![](https://velog.velcdn.com/images/choil8228/post/e6ba1b76-eb67-4bf5-862f-7513e3d0e913/image.png)

![](https://velog.velcdn.com/images/choil8228/post/abbce8c7-c60f-4da1-bf2d-822e1ceb6192/image.png)

![](https://velog.velcdn.com/images/choil8228/post/b8b61c91-a2c3-4330-b6ae-7cc66e8f62a1/image.png)

![](https://velog.velcdn.com/images/choil8228/post/2b878475-d66d-4e8a-9a26-304c034eeb14/image.png)


## 1번 [풀이 및 결과]

![](https://velog.velcdn.com/images/choil8228/post/1e23612a-c9b5-41bf-824a-0a91a62197c1/image.png)

![](https://velog.velcdn.com/images/choil8228/post/c1a6ab63-c715-4f0c-a902-c34cae2bc1e2/image.png)

---
## 2. [문제]

### 2-1. [문제]

![](https://velog.velcdn.com/images/choil8228/post/b912bcdc-b33e-4505-ae76-c696750785a6/image.png)

![](https://velog.velcdn.com/images/choil8228/post/ba3370f9-a49c-44c0-a90f-8d8ce6b7862a/image.png)

### 2-1. [풀이 및 결과]

![](https://velog.velcdn.com/images/choil8228/post/0e486e47-e637-454f-ba62-e1c1b7f12d27/image.png)

```
 if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new InvalidRequestException("이미 존재하는 이메일입니다.");
        }
```
#### 이 코드를 그냥 위로 올려버림
------

### 2-2. [문제]

![](https://velog.velcdn.com/images/choil8228/post/f529cbc7-95b3-4970-a8f1-f18904e1ff43/image.png)

### 2-2. [풀이 및 결과]

![](https://velog.velcdn.com/images/choil8228/post/5006813a-5cff-4b26-bd0f-1ddbd92f8e4f/image.png)

----

### 2-3. [문제]

![](https://velog.velcdn.com/images/choil8228/post/dab758ca-0827-40f8-83c5-a87fefa8ee58/image.png)


### 2-3. [풀이 및 결과]

![](https://velog.velcdn.com/images/choil8228/post/63daf29a-2dd5-465d-a836-c5c8ab77a9e3/image.png)
```
. : 아무 문자 1개
* : 0번 이상 반복
```
![](https://velog.velcdn.com/images/choil8228/post/7da90234-a06e-4521-944d-56f7b531ffb3/image.png)

## 비상
인식이 안되는 상황이 발생
![](https://velog.velcdn.com/images/choil8228/post/c644fcae-09ae-45b7-be6d-e9ee9cb95bb3/image.png)

#### 은 원래 이런 거라고 종범 튜터님이 말씀 해주심(큰 문제 아님)

>#### 꺼이꺼이
테스트를 해보는데 계속 asd12345678 **대문자** 안 넣어도 통과가 되길래 문제가 뭔가 했더니
**0-9** 이렇게 써야 하는데 **0~9** 이렇게 작성했음.
추가로 Controller에 @Valid를 안붙여서 그냥 통과가 됐던 거였음
결론: 쫌 꼼꼼히 하자....

-------

## 3. [문제]

![](https://velog.velcdn.com/images/choil8228/post/1891db74-1806-4e0a-a0c9-889039a9b190/image.png)
![](https://velog.velcdn.com/images/choil8228/post/9687fd8c-8b30-43b9-bc96-2c853616a9b6/image.png)

## 3. [풀이 및 결과]

![](https://velog.velcdn.com/images/choil8228/post/87702d4a-addd-4647-8c36-13fd87fc3102/image.png)

![](https://velog.velcdn.com/images/choil8228/post/05718b82-4ce9-4e00-9e82-feafc1e5f8af/image.png)

뭔 차이인지 잘 모르겠음

-----
## 4. [문제]

### 4-1. [문제]

![](https://velog.velcdn.com/images/choil8228/post/db0c8e9b-077d-46d2-88a1-d44f4462f6ac/image.png)

![](https://velog.velcdn.com/images/choil8228/post/3887d617-374c-49e0-92ed-a3d251670a50/image.png)

### 4-1. [풀이 및 결과]

![](https://velog.velcdn.com/images/choil8228/post/ee5e88cb-449d-429b-b79a-8c9262ae6a39/image.png)

![](https://velog.velcdn.com/images/choil8228/post/3130c866-3a05-4d66-8b9e-6d5867cb24f2/image.png)

-----
### 4-2. [문제]

![](https://velog.velcdn.com/images/choil8228/post/270cb520-8194-4e0e-a9f4-75aa5bf6d064/image.png)

![](https://velog.velcdn.com/images/choil8228/post/d4fc3fc8-03f4-4c7b-9bd1-57a2a621fe9c/image.png)

### 4-2. [풀이 및 결과]

![](https://velog.velcdn.com/images/choil8228/post/a1176340-3aee-4c47-9951-f65def165403/image.png)

----

### 4-3. [문제]

![](https://velog.velcdn.com/images/choil8228/post/2d04032d-51b7-4464-9b7c-7a2161022d0d/image.png)

### 4-3. [풀이 및 결과]

![](https://velog.velcdn.com/images/choil8228/post/2a00bf5a-0d4b-41e0-9ad3-6ad5316195fd/image.png)

![](https://velog.velcdn.com/images/choil8228/post/364d567b-09ee-4683-be2d-151b9e977c51/image.png)

------
## 5. [문제]



![](https://velog.velcdn.com/images/choil8228/post/d0a629ee-c40c-41ac-97ca-9075b0bc65fd/image.png)

![](https://velog.velcdn.com/images/choil8228/post/9d7881ec-8584-4091-9264-c6135f26697e/image.png)

왜 500이 나오는거지?

## 5. [풀이 및 결과]

![](https://velog.velcdn.com/images/choil8228/post/07a28701-e88c-4a10-bdf7-b236a8885334/image.png)


![](https://velog.velcdn.com/images/choil8228/post/c82b2f3a-af19-4418-8949-88f7d200d08b/image.png)



#### ADMIN으로 들어가면 잘 진행이 되지만 USER로 실행시 필터에서 막는 것 같다.
![](https://velog.velcdn.com/images/choil8228/post/b138d386-7a80-4662-8bb2-21e3bb50e73b/image.png)

#### 그래서 주석처리

![](https://velog.velcdn.com/images/choil8228/post/f8d09efd-2d2a-421a-9a96-574bbc555cab/image.png)

#### 잘나오는 모습

------

# 6. [문제]

## 6-1. [문제 인식 및 정의]

**JwtFilter**에서 **JWT**의 **userRole** 값을 **UserRole enum**으로 변환해서 사용하고 있었지만, **request attribute**에는 다시 문자열로 저장하고 있었다.
이로 인해 이후 **AdminCheckInterceptor**에서 다시 문자열을 **enum**으로 변환해야 했고, 같은 역할 정보가 서로 다른 타입으로 처리되어 **코드 흐름이 일관되지 않았다.**
기능 자체는 동작할 수 있었지만, 타입 혼용으로 인해 **유지보수성**과 **가독성** 측면에서 개선이 필요하다고 판단했다.

![](https://velog.velcdn.com/images/choil8228/post/7fe3f7eb-4605-4949-b8e8-0dbd9a79f4ac/image.png)

![](https://velog.velcdn.com/images/choil8228/post/e44f6a99-8f44-4b4c-96cb-e6db0c219849/image.png)

-----

## 6-2. [해결 방안]
### 6-2-1. [의사결정 과정]

문제의 원인은 **userRole**을 한 번은 **enum**으로, 한 번은 **문자열**로 다루고 있다는 점이라고 판단했다.
이미 **JwtFilter**에서 **UserRole enum**으로 변환한 값이 있으므로, 이후에도 **동일한 타입**을 유지하는 것이 가장 간단하고 자연스러운 해결 방법이라고 생각했다.
따라서 **request attribute**에도 문자열이 아닌 **enum 자체를 저장**하고, **Interceptor**에서도 이를 그대로 꺼내 사용하도록 수정하기로 했다.

### 6-2-2. [해결 과정]

기존에는 아래와 같이 **userRole**을 **enum**으로 만든 뒤, **request**에는 다시 문자열로 저장하고 있었다.

```
UserRole userRole = UserRole.valueOf(claims.get("userRole", String.class));
httpRequest.setAttribute("userRole", claims.get("userRole"));
```

이를 아래와 같이 수정하여 **enum 값을 그대로 저장**하도록 변경했다.

```
UserRole userRole = UserRole.valueOf(claims.get("userRole", String.class));
httpRequest.setAttribute("userRole", userRole);
```

또한 **AdminCheckInterceptor**에서도 문자열을 다시 변환하는 방식 대신, **request**에 저장된 **enum** 값을 그대로 꺼내 사용하도록 수정했다.


#### 기존:

```
UserRole role = UserRole.of((String) request.getAttribute("userRole"));
```

#### 수정:

```
UserRole role = (UserRole) request.getAttribute("userRole");
```

이렇게 수정하여 **userRole**이 **JwtFilter**부터 **Interceptor**까지 **동일한 타입**으로 전달되도록 정리했다.

![](https://velog.velcdn.com/images/choil8228/post/bfb7f6b7-c995-45f8-b734-d32aba5ec716/image.png)

![](https://velog.velcdn.com/images/choil8228/post/05b456be-b555-4f0e-acfe-169726f1f8f8/image.png)

------

## 6-3. [해결 완료]
### 6-3-1. [회고]

이번 작업을 하면서 느낀 점은, 기능이 동작하더라도 다른 사람이 작성한 코드를 이해하고 흐름을 파악하는 일은 생각보다 쉽지 않다는 것이었다.
특히 타입이 섞여 있는 작은 차이도 바로 보이지 않아, 코드를 더 꼼꼼하게 읽는 연습과 기본기에 대한 공부가 더 필요하다고 느꼈다.

------------------

### 6-3-2. [전후 데이터 비교]

#### 전:

![](https://velog.velcdn.com/images/choil8228/post/76f619f3-7cfb-4b00-b651-9d4158dab132/image.png)

![](https://velog.velcdn.com/images/choil8228/post/5ca0f33a-54a7-46e9-af9f-270b13048263/image.png)

----------

#### 후:

![](https://velog.velcdn.com/images/choil8228/post/dc8863b2-5941-43d2-8295-0363abe0edbb/image.png)

![](https://velog.velcdn.com/images/choil8228/post/ff2c02f7-78b8-4faf-a497-048e938cfe40/image.png)

![](https://velog.velcdn.com/images/choil8228/post/37f4d02c-5dca-406f-8e7a-fe7e2524aa90/image.png)

출력까지 잘 나온다!!

-----
## 7. [문제]
![](https://velog.velcdn.com/images/choil8228/post/accce0a3-9e23-4f18-b453-cf0cc36d3e93/image.png)

#### GPT를 사용해서 진행하였습니다.



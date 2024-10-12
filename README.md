<div align=center>
<img src="https://capsule-render.vercel.app/api?type=waving&color=auto&height=200&section=header&text=CookieTalk&fontSize=75" />
</div>
<div align=center>
	<h3>📚 Backend Tech Stack 📚</h3>
</div>
<div align="center">
	<img src="https://img.shields.io/badge/Java-007396?style=flat&logo=Conda-Forge&logoColor=white" />
  <img src="https://img.shields.io/badge/JPA-11DAFB?style=flat" />
  <img src="https://img.shields.io/badge/QueryDSL-4479A1?style=flat" />
  <br>
	<img src="https://img.shields.io/badge/Spring-6DB33F?style=flat&logo=Spring&logoColor=white" />
  <img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=flat&logo=SpringBoot&logoColor=white" />
  <img src="https://img.shields.io/badge/SpringSecurity-6DB33F?style=flat&logo=SpringSecurity&logoColor=white" />
  <br>
  <img src="https://img.shields.io/badge/Docker-2496ED?style=flat&logo=Docker&logoColor=white" />
  <img src="https://img.shields.io/badge/AmazonS3-569A31?style=flat&logo=AmazonS3&logoColor=white" />
  <img src="https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=MySQL&logoColor=white" />
  <img src="https://img.shields.io/badge/Redis-FF4438?style=flat&logo=Redis&logoColor=white" />
</div>
<div align=center>
	<h3>📚 Frontend Tech Stack 📚</h3>
</div>
<div align="center">
  <img src="https://img.shields.io/badge/React-61DAFB?style=flat&logo=React&logoColor=white" />
</div>

<div align=center>
	<h3>🛠️ Tools 🛠️</h3>
</div>
<div align="center">
	<img src="https://img.shields.io/badge/intellij-000000?style=flat&logo=intellijidea&logoColor=white" />
  	<img src="https://img.shields.io/badge/Git-F05032?style=flat&logo=Git&logoColor=white" />
  	<img src="https://img.shields.io/badge/GitHub-181717?style=flat&logo=GitHub&logoColor=white" />
  <img src="https://img.shields.io/badge/WebStorm-000000?style=flat&logo=WebStorm&logoColor=white"
</div>

</p>
  <p align="center">
    <a href="https://https://github.com/Nameless1004/cookietalk/graphs/contributors">
      <img alt="GitHub Contributors" src="https://img.shields.io/github/contributors/Nameless1004/cookietalk" />
    </a>
    <br />
      <br />
  </p>
</div>

# 📕쿠키톡 프로젝트
## 쿠키톡이란?
쿠키톡은 아침 커피 한 잔과 함께 즐길 수 있는 짧고 유익한 테크톡 영상을 제공하는 플랫폼입니다. 가볍게 먹는 쿠키처럼 부담 없이 시청할 수 있는 이 영상들은, 바쁜 일상 속에서도 쉽게 정보를 얻고 영감을 받을 수 있도록 설계되었습니다.

## 쿠키
쿠키는 쿠키톡에서 제공하는 짧은 테크톡 영상을 의미합니다. 간결하지만 핵심을 찌르는 내용을 담고 있어, 언제 어디서든 손쉽게 시청할 수 있습니다. 마치 바쁜 아침에 간단하게 즐기는 쿠키처럼, 짧지만 알찬 정보와 인사이트를 제공합니다.

## 인원
* 백엔드
  * 정재호
* 프론트엔드
  * 김민영
## 영상
<a href="" target='_blank'><img src="https://img.shields.io/badge/youtube-FF0000?style=flat&logo=youtube&logoColor=white" /></a>

<br>
<br>

# 백엔드
## API 명세서
https://living-offer-87d.notion.site/API-10ebf8163158803dbe99d0c2f7df1d25
## ♻️ERD

## 🗒️기능
### hls 영상 스트리밍
* 쿠키 업로드 시 영상을 m3u8파일로 변환 후 S3에 업로드
### JWT 토큰
* jwt 액세스, 리프레쉬 토큰 발행
* jwt 액세스, 리프레쉬 토큰 재발행
* jwt 토큰 인증, 인가
* 로그인 / 로그아웃
### 시리즈
* 시리즈 CRUD
### 채널
* 채널 CRUD
### 쿠키
* 쿠키 CRUD
* 쿠키 페이징 조회
* 쿠키 커서기반 조회
* 최근 본 쿠키 목록 조회
* 조회수 증가
### 추천 랜덤 재생

# 백엔드 성능 개선
## 영상 업로드 성능 개선
### 개선 배경
현재 업로드 프로세스는 동기적으로 처리되고 있으며, 특히 MP4 파일을 M3U8 포맷으로 변환하고 이를 Amazon S3에 업로드하는 과정에서 응답 시간이 길어졌습니다. 이 문제는 영상 파일의 크기가 커질수록 더욱 두드러지며, 사용자 경험에 부정적인 영향을 미칠 수 있다 생각하였습니다. 따라서 보다 효율적인 방법으로 이 프로세스를 개선하고자 하였습니다.
### 개선 방향
#### 비동기 처리 도입
* 성능 개선을 위해 **@Async**를 사용하여 파일 변환과 업로드 작업을 비동기적으로 처리하였습니다.
#### 실패 처리 로직
* 비동기 작업 중 에러가 발생하더라도 전체 쿠키 생성 트랜잭션을 **rollback**하는 대신, **상태를 FAILED**로 변경되게 하였습니다.
#### EventPublisher 활용
* 쿠키 업로드 완료 후 COMPLETED 상태로 변경하도록 이벤트를 구현하기 위해 EventPublisher와 RabbitMQ에 대해서 레퍼런스를 찾았습니다.

|구분|EventPublisher |RabbitMQ|
|-|-|-|
|처리 방식| 로컬 이벤트, 같은 JVM 내에서 처리 |메세지 브로커를 통한 비동기 처리|
|확장성|낮음, 같은 JVM내에서만 동작|높음, 분산 시스템에 적합|
|신뢰성|낮음, 이벤트 휘발성|넢음, 메세지 큐 사용 및 재시도 가능|
|설정 및 관리|간단, Spring 내장 기능 사용|복잡, 외부 메세지 브로커 설정 필요|
|애플리케이션 규모|소규모 애플리케이션에 적합|대규모 및 분산 시스템에 적합|
|장애 복원력|없음, 실패 시 재시도 불가|높음, 메세지 큐에 메세지 저장 및 재처리|
|성능 오버헤드|적음, 메세지 브로커 필요 X|높음, 네트워크와 브로커 사용|

* 종합해봤을 때 현 프로젝트는 소규모, 분산 시스템이 아니므로 RabbitMQ대신 장애 복원력이 없더라도 EventPublisher를 사용하는 것이 적합하다 판단하였습니다.
* EventPublisher를 사용하여 업로드 작업이 성공적으로 완료되면 자동으로 쿠키의 상태를 **COMPLETED**로 변경하게 하였습니다.

### 결과
#### 개선 전
![nonasync일때](https://github.com/user-attachments/assets/4a8e0da7-38bd-4069-a04e-33488997c94f)
#### 개선 후
![async일때](https://github.com/user-attachments/assets/9ad0efcc-4f15-445d-aad0-90b5a0063fb2)


<details>
	<summary><h1>💥트러블 슈팅</h1></summary>
<!-- 	<img src= "https://github.com/user-attachments/assets/c4ec07dc-3521-4220-a356-85d67a1ccfa1"/>
 	<img src="https://github.com/user-attachments/assets/2b5cdd19-6c9a-4562-9443-dd5a65612633"/> -->
</details>



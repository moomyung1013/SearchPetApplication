 # 딥러닝 기반 반려동물 검색 애플리케이션
  본 시스템은 협업하여 만든 시스템으로 프로젝트 참가자의 개발내용은 아래에서 확인할 수 있다.
  |개발자|역할|Hompage|Github|
  |:---:|:---:|:---:|:---:|
  |moomyung1013|`앱개발, DB, 서버개발, 크롤링, UI/UX`|http://velog.io/@moomyung1013/ |https://github.com/moomyung1013 |
  |jiyeon2781|`앱개발, DB, 서버개발, DeepLearning Model 개발, UI/UX`|https://velog.io/@jiyeon2781 |https://github.com/jiyeon2781 |
  |TaeHyangKwon|`앱개발, DB, 서버개발, UI/UX`|-|https://github.com/TaeHyangKwon |

<br><br>
# 시스템 개요
  안드로이드 스튜디오를 기반으로 애플리케이션을 구현하였다.
  <br>키우던 반려동물을 잃어버린 사용자가 반려동물 검색을 원할 시 사진과 잃어버린 장소 등에 대한 데이터를 애플리케이션에 전달하고 딥러닝 모델은 전달받은 이미지를 입력으로 받아 검색을 진행한다. 모델의 출력으로 나온 상위 5개의 데이터는 데이터베이스에 저장되며 결과는 애플리케이션에 리스트로 출력된다. 딥러닝 모델은 검색 데이터로 다음 두가지를 활용한다. 첫번째는 동물 보호 관리시스템 홈페이지로부터 주기적으로 크롤링하여 저장한 데이터이며, 두번째는 길거리에서 유기동물을 발견한 일반인이 등록한 데이터이다.
  <br>이렇게 사용자는 자신이 잃어버린 반려동물을 입력하면 딥러닝 모델이 검색을 하여 결과를 출력해주기 때문에 기존의 시스템보다는 빠르고 편리하게 결과를 확인할 수 있다. 반려동물 검색 이외에도 본 애플리케이션에서는 입양 추천, 커뮤니티, 유기동물 리스트 출력 등의 기능을 지원한다.

<br><br>

# 개발 도구
  Server, Database, Crawling 개발은 통합개발환경인 Goorm IDE(Linux)에서 진행하였다.
  - Server : Node.js
  - Database : MySQL
  - Crawling : Python
  - Deep Learning Model : Tensorflow Framework
  - Application : Android Studio(kotlin)

<br><br>
# 실행 방법
  1. 댕댕아어디있니.apk 파일을 설치할 기기에 저장
  2. 댕댕아어디있니.apk 파일을 실행해 설치 (보안상의 문제가 뜨면 무시하고 설치 진행)
  3. 데이터베이스 실행(service mysql start)
  4. 서버 실행(node potato.js)
  5. 검색 모델 실행(python sql_connect.py)
  6. "댕댕아 어디있니" 애플리케이션 실행
  
  ★ 애플리케이션 실행 전 데이터베이스와 서버를 동작시켜야 어플 내의 모든 기능 정상 수행됨
  
  ## 부가 설명
  데이터베이스는 따로 백업용 데이터 파일을 저장하는 부분이 존재하지 않아 파일로 저장하지 못함. 따라서 정보 데이터(크롤링된 보호소 동물 데이터, 커뮤니티 데이터 등)는 본 자료에 존재하지 않음.

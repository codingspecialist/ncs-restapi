# NCS 평가 프로그램

## 급한것

- [x] 학생쪽 디자인 완료
- [x] Document 디자인 일관성 맞추기
- [ ] 중탈, 중탈이유, 훈련수준, 학생총평 관리 (학생관리 메뉴 필요)
- [ ] 자바스크립트에서 사용하는 css는 디자인과 연관없게 만들기
- [ ] css 정리하기

## 단축키

command+shift+[ 탭변경
command+shift+] 탭변경
option+shift+마우스커서 다중선택
control+tab 이전 탭 가기

## 리팩토링 시작

### 개발방법

- [ ] 구글 줄 사용해서 개발하기
- [ ] GPT API와 파이썬 MCP 연결해서 시험 결과 시각화
- [ ] 결과 시각화 페이지를 위한 전용 테이블 만들기 (통계 값 반영)

### 레코드 레이어 추가

- [x] 도메인 (엔티티, 서비스, 레포지토리, 레코드)
- [x] 레코드 (DB -> Repository) 상황에서 엔티티에 매핑안되는 복잡한 네이티브 쿼리일때
- [x] 레코드 (Service -> Controller)

### 1. 유저와 강사

- [x] User, Teacher 정규화
- [x] 관련 버그 및 인증 로직 수정
- [x] 인증번호 확인 로직 (과정 상세보기의 해당 과정의 학생들)
- [x] 유저 관련 조회 쿼리 정리
- [x] 유저 관련 주소 정리

### 2. 유저와 학생

- [x] User, Student 정규화
- [x] 관련 버그 및 인증 로직 수정
- [x] 학생 관련 주소 정리
- [x] 학생 등록시에 당연히 courseId번 과정으로 선택되어야 한다.
- [x] 학생 관련 조회 쿼리 정리
- [x] 학생 관련 view 정리
- [x] Course 컨트롤러에 관련된것들 옮기고, 서비스만 도메인에서 실행하기

### 3. 과정 관리

- [x] TODO: 이부분 currentIndex, fExamId 등등 복잡하게 수정해야함
- [x] 과정별로 메인강사, 보조강사들 등록하는 로직 필요
- [x] 과정 상태 변경 스케쥴러 등록 (새벽3시에 갱신됨)
- [x] 로그인한 강사가 속해 있는 과정만 보이게 하기
- [x] 주소 정리 (student, subject tab=0 이부분 수정해야함)
- [x] 하나의 과정에서는 여러명의 강사들이 속해있을 수 있다.
- [x] 해당 강사들은 다른 과정에도 속해있을 수 있다.
- [x] 과정에 등록되어 있는 강사들은 해당 과정을 조회할 수 있다.
- [x] detail 화면 쿼리 정리해야한다. (교과목과, 학생들 조회)
- [x] 한방쿼리로 다른 부분도 수정하자.
- [x] 교과목 요소등록시 순번 중복안되게 수정하기

### 4. 시험지 관리

- [x] 같은 시험지에 본평가가 2개 이상 존재하는 것 막기
- [x] 시험지 번호 다시 관리하기

### 5. 시험 관리 [이거해야함!!!!!!!!!!!!!!!!! 2025.06.03]

- [x] 학생별, 선생별 컨트롤러 나누기
- [x] DTO 정리하기
- [x] 쿼리 정리하기
- [x] view 정리하기

### 6. 문서 관리

- [x] 주소 정리 완료
- [ ] 6번, 7번 문서 완료하기

### 7. 기존 교과목, 시험지 불러와서 깊은 복사

### 8. 객관식, 서술형, 포트폴리오/작업형

- [ ] 시험지를 종류별로 만들 수 있게 하기
- [ ] 재평가/결시자 0.9할껀지 0.8할껀지 교과목 등록할 때 정하기 (서비스에도 연결하기)
- [ ] 작업형/서술형을 노션으로 받으면 선생님은 혹시 모르니까 노션 화면 캡쳐해서 보관할 수 있게 하기
- [ ] document에 저장기능 만들어서 해당 내용 json으로 한방에 저장하기 (json 데이터 있으면 캐시하고, 리로드 버튼 만들어서 다시 불러올 수 있게 로직 짜기)

### 마지막

- [ ] 쿼리 최종 체크
- [ ] 중복, 프론트, 서버 유효성 검사 해야함
- [ ] 컨트롤러 함수명, 서비스 함수명 정리
- [ ] 수정
- [ ] 삭제
- [ ] validation 체크
- [ ] 사용하지 않는 파일 삭제
- [ ] css 파일 분리 [script에서 사용하는 class명 디자인과 무관하게 만들기, css 재사용 분리하기]
- [ ] js 파일 분리

### KDT PBL 평가 필요

- [ ] Paper, Question(총점수 point), QuestionOption(isRight 삭제하고, point만들어서 점수주기), 평가기준(실패기준이 될수도)도 옵션마다!!
- [ ] exam_tb (단답형 시험)
- [ ] evaluation_tb (pbl 평가, 작업형 평가) 두개 따로 분리
- [ ] subject_tb (프로젝트 교과목)
- [ ] team_tb, team_member_tb, student_tb(학생1, 학생2, 학생3) 프로젝트1 팀이 만들어지고, team_tb에 맴버로 종속
- [ ] subject_element_tb (발표요소, 협업요소, 커뮤니케이션요소)

### 추후 리팩토링 v3

- [ ] 시험지, 문서시험지, 학생시험지 생긴 형태 동일하게 하기
- [ ] 시험지의 문제마다 평가기준 붙여두기 (위에 따로 붙여두지 않기)
- [ ] 유효성 검사 체크하기
- [ ] PDF 인쇄 최적화 도움 받기 (GPT)
- [ ] 시큐리티 적용
- [ ] 컨트롤러는 기능중심, domain은 도메인 중심
- [ ] 기관별 과정으로 변경
- [ ] 관리자 기능 만들기
- [ ] 결재 및 구독 방식 만들기
- [ ] 보고서 캐싱기능
- [ ] 엔티티가 없는 컨트롤러의 서비스는 XViewService로 만들기
- [ ] view의 구조는 web의 구조와 일치시키기
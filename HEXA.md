# 헥사고날 아키텍처

ssr 프로젝트는 domain에 모든걸 두고, web에 controller만 둬서 화면의 메뉴에 맞게 만드는게 좋다!!

하지만!!!

향후 마이크로서비스 아키텍처(MSA)로의 확장 가능성을 조금이라도 염두에 두고 있다면, 고민할 필요 없이 '기능으로 묶는 방식'이 전략적으로 훨씬 더 유리한 선택입니다.
즉 hexa-다묶기가 좋다는 뜻

기능으로 묶으면 테스트코드 작성할때는 adapter 부분만 빼면 단위테스트가 mock으로 가능하다.

컨트롤러 서비스 관계에서

```text
요청 -> 엔티티로 전달 가능해도 서비스로 Command를 전달한다. (외부에서 들어오는 데이터는 신뢰할 수 없는 데이터)
응답 -> 엔티티로 전달 가능하면 컨트롤러로 엔티티를 전달한다. 안되면 Result객체 (안에서 나가는 데이터는 안전한 데이터)
```

1. UseCase (인터페이스) 👉 application/port/in
   역할: 애플리케이션이 "무엇을 할 수 있는지"를 정의하는 기능 명세서입니다.

위치: application 계층의 입구(inbound port)에 위치하여, "우리 서비스는 이런 기능들을 제공합니다"라고 외부에 알려줍니다.

2. Service (구현체) 👉 application/service
   역할: UseCase 인터페이스의 실제 비즈니스 로직을 구현합니다.

위치: application의 핵심 로직을 담당하는 service 패키지에 위치합니다.

3. Command 객체 👉 application/dto
   역할: UseCase를 실행하는 데 필요한 데이터들을 담는 객체입니다. 외부(컨트롤러)에서 넘어온 데이터를 서비스가 이해할 수 있는 형태로 가공한 것입니다.

위치: application 계층의 입력 값(DTO)이므로 application/dto에 위치합니다.

4. Request/Response DTO 👉 adapter/in/web/dto
   역할: 오직 웹(HTTP) 계층에서만 사용하는 데이터 객체입니다. JSON 직렬화/역직렬화를 위한 @RequestBody, @ResponseBody와 함께 사용됩니다.

위치: 웹 어댑터(adapter/in/web)에 종속적인 클래스이므로 해당 패키지 내 dto 폴더에 위치합니다.

흐름: UserController는 UserSignupRequest를 받아서 UserSignupCommand로 변환한 뒤, UserSignupUseCase를 호출합니다. 이 과정을 통해 웹 계층과 응용 계층이
완벽하게 분리됩니다.

네, 포트의 이름을 짓는 데는 몇 가지 일반적인 규칙이 있습니다.

가장 중요한 원칙은 **애플리케이션 코어가 '무엇을 원하는지'**를 비즈니스 관점에서 설명하는 것입니다. '어떻게' 구현하는지(예: JPA, Kafka)는 이름에 드러나지 않아야 합니다.

## 데이터 조회 (Data Retrieval) 포트

외부(DB 등)에서 데이터를 가져올 때 사용하는 포트입니다.

Load...Port: ID를 이용해 특정 도메인 객체 하나를 로드할 때 주로 사용합니다.

예시: LoadAccountPort, LoadUserPort

Find...Port: ID 외에 다른 조건으로 데이터를 찾거나 목록을 조회할 때 사용합니다.

예시: FindUserByEmailPort, FindAllOrdersPort

Query...Port: Find보다 더 일반적이거나 복잡한 조회를 나타낼 때 사용합니다.

예시: QueryAccountActivitiesPort (계좌의 모든 거래 내역 조회)

## 데이터 변경 (Data Mutation) 포트

외부(DB 등)에 데이터의 생성, 수정, 삭제를 요청할 때 사용합니다.

Save...Port: 새로운 데이터를 저장할 때 주로 사용합니다.

예시: SaveUserPort, SaveOrderPort

Update...Port 또는 Update...StatePort: 기존 데이터의 상태를 변경(수정)하는 것을 명시할 때 사용합니다.

예시: UpdateAccountStatePort (계좌의 잔액 등 상태를 업데이트)

Register...Port: '생성'이라는 행위를 비즈니스 용어(등록)로 표현할 때 사용합니다.

예시: RegisterCoursePort

## 외부 시스템 호출 (External System Call) 포트

DB 외에 다른 외부 시스템(메시지 큐, 외부 API, 이메일 서버 등)과 통신할 때 사용합니다.

Publish...EventPort 또는 Send...EventPort: 메시지 큐(Kafka, RabbitMQ 등)에 이벤트를 발행할 때 사용합니다.

예시: PublishOrderCreatedEventPort (주문 생성 이벤트를 발행)

Notify...Port: 사용자에게 알림(이메일, SMS 등)을 보낼 때 사용합니다.

예시: NotifyUserByEmailPort

Request...Port: 외부 서비스의 API를 호출하고 응답을 받아야 할 때 사용합니다.

예시: RequestPaymentPort (결제 게이트웨이에 결제를 요청)

Save...Port: "이 포트는 데이터를 DB에 저장하는 역할을 한다"는 기술적인 의미가 강합니다. 단순한 CRUD 작업에서는 명확하고 직관적입니다.

Register...Port: "사용자 등록"이라는 비즈니스 행위 자체를 의미합니다. 이 포트의 구현체는 단순히 DB에 저장하는 것 외에, 환영 이메일을 보내거나, 추천인 포인트를 적립하는 등 '등록'과 관련된 여러
작업을 포함할 수 있습니다.

## "포함" 관계 vs "참조" 관계

1. ExamAnswer는 Exam에 포함됩니다 (Composition)
   생명주기가 같습니다. Exam이 사라지면 ExamAnswer는 의미가 없습니다. ExamAnswer는 Exam의 일부입니다.

이것이 바로 ExamAnswer를 exam 패키지 내부에 두는 이유입니다. 우리는 이것을 하나의 덩어리, 즉 애그리거트(Aggregate) 라고 부릅니다.

2. Exam은 Subject를 참조합니다 (Association)
   생명주기가 다릅니다.

Subject(교과목)는 아직 시험이 만들어지지 않았어도 독립적으로 존재할 수 있습니다.

Exam(시험)도 Subject가 있어야만 만들어지지만, Exam이 Subject의 일부는 아닙니다. Exam은 "어떤 교과목에 대한 시험"인지 가리키고(참조) 있을 뿐입니다.

Subject와 Exam은 각각 독립적으로 중요한 비즈니스 개념입니다. 따라서 각각을 독립적인 도메인 패키지로 분리하는 것이 좋습니다.

## 쉬운 비유

자동차와 바퀴: "바퀴"는 "자동차"의 일부입니다. 자동차 없이는 의미가 없죠. (Exam과 ExamAnswer의 관계)

자동차와 운전자: "운전자"는 "자동차"의 일부가 아닙니다. 운전자는 자동차를 **사용(참조)**할 뿐, 각자 독립적으로 존재할 수 있습니다. (Exam과 Subject의 관계)

따라서 Exam과 Subject처럼, 서로 관계는 있지만 각자 독립적인 비즈니스 개념이라면 별도의 도메인 패키지로 분리하는 것이 시스템의 결합도를 낮추고 유연성을 높이는 더 좋은 방법입니다.

com/your/project/
├── user/ # 👤 사용자, 인증, 권한
│ ├── domain/
│ │ ├── User.java
│ │ ├── Student.java
│ │ ├── Teacher.java
│ │ └── Emp.java # 직원 (User의 한 종류)
│ └── ... (application, adapter)
│
├── course/ # 📚 과정, 수강
│ ├── domain/
│ │ ├── Course.java
│ │ └── CourseTeacher.java # 과정-강사 매핑 정보
│ └── ...
│
├── subject/ # 📖 교과목, 학습요소
│ ├── domain/
│ │ ├── Subject.java
│ │ └── SubjectElement.java
│ └── ...
│
├── paper/ # 📋 시험지 (템플릿)
│ ├── domain/
│ │ ├── Paper.java
│ │ ├── PaperQuestion.java
│ │ └── PaperQuestionOption.java # (아래 추가 설명 참고)
│ └── ...
│
└── exam/ # ✍️ 시험 응시 및 채점
├── domain/
│ ├── Exam.java
│ ├── ExamAnswer.java
│ └── ExamResult.java
└── ...
# Clou.D Backend

### **코드 작성**
최소한의 기능단위로 테스트 코드 작성하기<br>
최소한의 기능단위로 커밋 메세지 작성하기

### **개발 절차**
**예시**<br>
github issue 생성하기<br>
→ feature/login branch 생성<br>
→ 로그인 기능 코드 추가<br>
→ add, commit<br>
→ feature/login push<br>
→ pr 생성<br>
→ 코드 리뷰, 충돌 검사<br>
→ feature 로 squash and merge<br>
→ feature/login 삭제<br>

## 엔티티 작성 규칙
- 생성자 주입 + 빌더 패턴
- getter 사용가능
- setter 금지 
  - setter 필요시 메서드 새로 생성하기
  - 생성한 메서드명은 set관련 불가!

## Branch
- main(배포용)
- dev(개발용)
- feature(기능 개발용)

## 대분류 패키지 Naming
- 메인: Main    
  - 메인화면 및 공통 관련 리소스 관리 
- 원무: Reception
  - 원무 데이터 관리
- 보험: Insurance
  - 보험 관련
- 통계: Statistics
  - 통계관련
- 검사: Examination
  - 검사관련
- 진료: Treatment
  - 진료관련
- 간호: Nursing
  - 간호관련

## 소규모 패키지 Naming
- config
- controller
- dto
- entity
- repository
- service
- status


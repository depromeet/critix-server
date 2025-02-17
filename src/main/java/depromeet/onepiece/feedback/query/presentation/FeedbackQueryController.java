package depromeet.onepiece.feedback.query.presentation;

/**
 * 도메인 간 의존성을 제거하고 이벤트 소싱을 적용하기 위해 CQRS 패턴을 적용해요. CQRS 패턴은 명령(Command)과 조회(Query)를 분리하는 패턴으로, 이 중에서
 * 조회(Query)는 상태를 조회하는 역할을 담당해요. Master-Slave 구조로 데이터베이스가 구성되어 있을 때, 조회 모델을 따로 분리하는 것이 조회 성능 향상에 도움이
 * 될 수 있어요.
 */
public class FeedbackQueryController {}

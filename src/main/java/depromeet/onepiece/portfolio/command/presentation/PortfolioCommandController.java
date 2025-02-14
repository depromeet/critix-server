package depromeet.onepiece.portfolio.command.presentation;

/**
 * 도메인 간 의존성을 제거하고 이벤트 소싱을 적용하기 위해 CQRS 패턴을 적용해요. CQRS 패턴은 명령(Command)과 조회(Query)를 분리하는 패턴으로, 이 중에서
 * 명령(Command)은 상태를 변경하는 역할을 담당해요. 꼭 CommandController라고 명명할 필요는 없지만, 명령을 처리하는 역할을 하는 컨트롤러라는 것을 알 수
 * 있도록 이름을 지어주는 것이 좋아요.
 */
public class PortfolioCommandController {}

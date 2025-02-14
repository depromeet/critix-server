package depromeet.onepiece.portfolio.query.domain;

/**
 * 도메인 주도 개발 DDD 아키텍처에서는 조회 성능 향상을 위해 QueryRepository를 별도로 구현하여 CQRS 패턴을 적용합니다. Master-Slave 구조에서는
 * Master DB에 쓰기 작업을 하고, Slave DB에서 읽기 작업을 하기 때문에, QueryRepository를 별도로 구현해요.
 */
public interface PortfolioQueryRepository {}

package depromeet.onepiece.portfolio.command.presentation.response;

import java.util.List;

public record ChatGPTResponseDto(
    OverallEvaluationDto 전체_평가,
    List<StrengthDto> 강점_분석,
    List<ImprovementDto> 개선할_점_및_해결방안,
    List<PageFixDto> 페이지_별로_수정이_필요한_부분을_찾았어요) {}

record OverallEvaluationDto(
    String 제목,
    EvaluationDto 직무_적합도,
    EvaluationDto 논리적_사고,
    EvaluationDto 문장_가독성,
    EvaluationDto 레이아웃_가독성) {}

record EvaluationDto(int 점수, String 평가) {}

record StrengthDto(String title, String 내용) {}

record ImprovementDto(String title, String 내용) {}

record PageFixDto(String 페이지번호, List<FixContentDto> contents) {}

record FixContentDto(String type, String 기존_문장, String 수정된_문장) {}

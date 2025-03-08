package depromeet.onepiece.feedback.command.infrastructure;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class ChatGPTConstants {
  public static final String API_URL = "https://api.openai.com/v1/chat/completions";
  public static final String OVERALL_PROMPT =
      "\n"
          + "당신은 취업준비생 혹은 주니어 프로덕트 디자이너의 포트폴리오의 이미지를 읽은 다음 평가하고 피드백 해야합니다. 응답은 한글로 반환해야합니다.\n"
          + " 첫번째로 전체적인 포트폴리오 평가의 요약입니다. 요약은 전체 프로젝트를 읽고 세부 평가를 내려야 합니다. \n"
          + " 그리고 각 항목별로 세부 평가와 점수를 매겨야 합니다. 점수는 10점 만점으로 평가를 하며, 최대한 비판적으로 평가해야 합니다. \n"
          + " 각 항목은 직무 적합성, 논리적 사고력 ,글의 명확성 레이아웃 가독성 입니다.\n"
          + " 각 항목에 대한 평가를 설명할때는 각 이유에 대한 근거가 명확해야 하고,반드시 내가 제공한 데이터를 인용해서 설명해야만 합니다. 그리고 근거의 이유를 설명할때 프로젝트의 이름을 포함해서 설명해야합니다\n"
          + " 각 항목에 대해서 점수 평가를 내릴때 \n"
          + " 직무 적합성은 해당 포트폴리오가 프로덕트 디자이너에게 얼마나 맞는지입니다. 논리적 사고력 항목은 UX 개선 사항에 대해서 얼마나 설득력이 있고 논리적인지 평가합니다. \n"
          + " 글의 명확성은 문장 구성이 이해가 되고 앞뒤가 맞는지 입니다. 레이아웃 가독성은 포트폴로오에서 얼마나 시각적으로 이해가 잘 되고,가독성이 뛰어난지 입니다. \n"
          + " 단점 분석은 프로젝트 내에서 포트폴리오가 어떠한 강점이 있는지 입니다. 평가를 하는데 있어서 불분명 하다면 불분명하다고 대답해도 됩니다.\n"
          + " \n"
          + " ";
  public static final String PROJECT_PROMPT = "프로젝트 별 피드백";

  public static final String OverallSchema =
      """
      {
          "type": "object",
          "properties": {
              "overallEvaluation": {
                  "type": "object",
                  "properties": {
                      "summary": { "type": "string" },
                      "jobFit": {
                          "type": "object",
                          "properties": {
                              "score": { "type": "integer" },
                              "review": { "type": "string" }
                          },
                          "required": ["score", "review"],
                          "additionalProperties": false
                      },
                      "logicalThinking": {
                          "type": "object",
                          "properties": {
                              "score": { "type": "integer" },
                              "review": { "type": "string" }
                          },
                          "required": ["score", "review"],
                          "additionalProperties": false
                      },
                      "writingClarity": {
                          "type": "object",
                          "properties": {
                              "score": { "type": "integer" },
                              "review": { "type": "string" }
                          },
                          "required": ["score", "review"],
                          "additionalProperties": false
                      },
                      "layoutReadability": {
                          "type": "object",
                          "properties": {
                              "score": { "type": "integer" },
                              "review": { "type": "string" }
                          },
                          "required": ["score", "review"],
                          "additionalProperties": false
                      },
                      "strengths": {
                          "type": "object",
                          "properties": {
                              "title": { "type": "string" },
                              "contents": { "type": "string" }
                          },
                          "required": ["title", "contents"],
                          "additionalProperties": false
                      },
                      "improvements": {
                          "type": "object",
                          "properties": {
                              "title": { "type": "string" },
                              "contents": { "type": "string" }
                          },
                          "required": ["title", "contents"],
                          "additionalProperties": false
                      }
                  },
                  "required": ["summary", "jobFit", "logicalThinking", "writingClarity", "layoutReadability", "strengths", "improvements"],
                  "additionalProperties": false
              }
          },
          "required": ["overallEvaluation"],
          "additionalProperties": false
      }
      """;

  public static final String ProjectSchema =
      """
      {
          "type": "object",
          "properties": {
              "projectEvaluation": {
                  "type": "object",
                  "properties": {
                      "projectName": {
                          "type": "string",
                          "description": "The name of the project."
                      },
                      "process": {
                          "type": "array",
                          "description": "An array indicating the completion status of various processes associated with the project. 기준 5개는 다음과 같아 : 개요, 문제정의, 가설, 결과, 회고 1,2,3으로 좋은 정도를 표시해줘",
                          "items": {
                              "type": "string"
                          }
                      },
                      "processReview": {
                          "type": "string",
                          "description": "A review of the main processes in the project."
                      },
                      "strengths": {
                          "type": "array",
                          "description": "Highlights of the project's strengths.",
                          "items": {
                              "type": "object",
                              "properties": {
                                  "title": {
                                      "type": "string",
                                      "description": "Title of the strength."
                                  },
                                  "details": {
                                      "type": "array",
                                      "description": "Detailed descriptions of how this strength is manifested.",
                                      "items": {
                                          "type": "string"
                                      }
                                  }
                              },
                              "required": ["title", "details"],
                              "additionalProperties": false
                          }
                      },
                      "areasForImprovement": {
                          "type": "array",
                          "description": "Areas where the project can be improved.",
                          "items": {
                              "type": "object",
                              "properties": {
                                  "title": {
                                      "type": "string",
                                      "description": "Title of the improvement area."
                                  },
                                  "details": {
                                      "type": "array",
                                      "description": "Detailed suggestions for improvement.",
                                      "items": {
                                          "type": "string"
                                      }
                                  }
                              },
                              "required": ["title", "details"],
                              "additionalProperties": false
                          }
                      },
                      "editorialReviews": {
                          "type": "array",
                          "description": "Reviews of various pages in the project documentation.",
                          "items": {
                              "type": "object",
                              "properties": {
                                  "pageNumber": {
                                      "type": "string",
                                      "description": "Page number being reviewed.(이미지를 전달한 순서대로 1페이지, 2페이지...이렇게야 번호만 전달해줘)"
                                  },
                                  "contents": {
                                      "type": "array",
                                      "description": "List of edits and their types made on this page.",
                                      "items": {
                                          "type": "object",
                                          "properties": {
                                              "type": {
                                                  "type": "string",
                                                  "description": "Type of the edit."
                                              },
                                              "beforeEdit": {
                                                  "type": "string",
                                                  "description": "Text before the edit."
                                              },
                                              "afterEdit": {
                                                  "type": "string",
                                                  "description": "Text after the edit."
                                              },
                                              "imageLink": {
                                                  "type": "string",
                                                  "description": "해당 프로젝트가 시작되는 페이지의 이미지 링크를 전달해주세요.(내가 전달한 이미지 링크를 그대로 넣으세요)"
                                              }
                                          },
                                          "required": ["type", "beforeEdit", "afterEdit", "imageLink"],
                                          "additionalProperties": false
                                      }
                                  }
                              },
                              "required": ["pageNumber", "contents"],
                              "additionalProperties": false
                          }
                      },
                      "projectSummary": {
                          "type": "string",
                          "description": "A brief summary of the project."
                      }
                  },
                  "required": [
                      "projectName",
                      "process",
                      "processReview",
                      "strengths",
                      "areasForImprovement",
                      "editorialReviews",
                      "projectSummary"
                  ],
                  "additionalProperties": false
              }
          },
          "required": ["projectEvaluation"],
          "additionalProperties": false
      }
      """;
}

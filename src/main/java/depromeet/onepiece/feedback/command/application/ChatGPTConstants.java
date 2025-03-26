package depromeet.onepiece.feedback.command.application;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class ChatGPTConstants {
  public static final String API_URL = "https://api.openai.com/v1/chat/completions";
  public static final String OVERALL_PROMPT =
      """
		  당신은 취업준비생 혹은 주니어 프로덕트 디자이너의 포트폴리오의 이미지를 읽은 다음 평가하고 피드백 해야합니다. 응답은 한글로 반환해야합니다.
		   첫번째로 전체적인 포트폴리오 평가의 요약입니다. 요약은 전체 프로젝트를 읽고 세부 평가를 내려야 합니다.
		   그리고 각 항목별로 세부 평가와 점수를 매겨야 합니다. 점수는 10점 만점으로 평가를 하며, 최대한 비판적으로 평가해야 합니다.
		   각 항목은 직무 적합성, 논리적 사고력 ,글의 명확성 레이아웃 가독성 입니다.
		   각 항목에 대한 평가를 설명할때는 각 이유에 대한 근거가 명확해야 하고,반드시 내가 제공한 데이터를 인용해서 설명해야만 합니다. 그리고 근거의 이유를 설명할때 프로젝트의 이름을 포함해서 설명해야합니다
		   각 항목에 대해서 점수 평가를 내릴때
		   직무 적합성은 해당 포트폴리오가 프로덕트 디자이너에게 얼마나 맞는지입니다. 논리적 사고력 항목은 UX 개선 사항에 대해서 얼마나 설득력이 있고 논리적인지 평가합니다.
		   글의 명확성은 문장 구성이 이해가 되고 앞뒤가 맞는지 입니다. 레이아웃 가독성은 포트폴로오에서 얼마나 시각적으로 이해가 잘 되고,가독성이 뛰어난지 입니다.
		   단점 분석은 프로젝트 내에서 포트폴리오가 어떠한 강점이 있는지 입니다. 평가를 하는데 있어서 불분명 하다면 불분명하다고 대답해도 됩니다.
		  """;
  public static final String PROJECT_PROMPT =
      """
	  다음 이미지는 디자이너의 포트폴리오입니다. 하나의 포트폴리오에 여러 개의 프로젝트가 포함되어 있을 수 있습니다. \s
	  이미지에서 **프로젝트별로 구분**하여 감지하고, 각 프로젝트에 대해 아래와 같은 형식으로 구조화된 피드백을 JSON 형태로 작성해 주세요.

	  다음 기준을 따라 주세요:

	  ---

	  1. 각 프로젝트마다 다음 정보를 포함해 주세요:
	    - `projectName`: 프로젝트 제목
	    - `process`: ["개요", "문제정의", "가설", "결과", "회고"] 5단계를 각각 `"GOOD"`, `"SOSO"`, `"BAD"` 중 하나로 평가
	    - `processReview`: 위 5단계에 대한 평가 이유를 2~3문장으로 서술
	    - `positiveFeedback`: 긍정적인 피드백 항목 3~4개 (각 항목은 `title`, `content` 리스트 형태로 구성)
	    - `negativeFeedback`: 개선이 필요한 피드백 항목 3~4개 (각 항목은 `title`, `content` 리스트 형태로 구성)
	    - `projectSummary`: 전체 총평

	  ---

	  2. 출력 형식은 다음 JSON 구조를 따라 주세요:

	  ```json
	  {
	    "projectEvaluation": [
	      {
	        "projectName": "",
	        "process": ["GOOD", "SOSO", "GOOD", "GOOD", "SOSO"],
	        "processReview": "",
	        "positiveFeedback": [
	          {
	            "title": "",
	            "content": ["", ""]
	          }
	        ],
	        "negativeFeedback": [
	          {
	            "title": "",
	            "content": ["", ""]
	          }
	        ],
	        "projectSummary": ""
	      }
	    ]
	  }

	  3. 예시 (예시는 참고만 해주세요.)
	  {
	    "projectEvaluation": [
	      {
	        "projectName": "토스 새소식 프로젝트",
	        "process": ["GOOD", "GOOD", "GOOD", "GOOD", "SOSO"],
	        "processReview": "전체적으로 문제 정의와 가설 설정이 명확하며, 실험과 결과 분석도 논리적으로 잘 구성되어 있습니다. 다만 회고 부분이 조금 더 구체적이고 깊이 있었으면 좋았겠다는 아쉬움이 있습니다.",
	        "positiveFeedback": [
	          {
	            "title": "데이터 기반의 문제 해결 접근",
	            "content": [
	              "리텐션 지표, CTW 분석 등을 통해 실제 사용자 행동을 기반으로 문제를 진단한 점이 매우 인상적임",
	              "문제 정의가 정량 데이터와 사용자 분석을 토대로 명확히 설정되어 신뢰도를 높였음"
	            ]
	          },
	          {
	            "title": "시즌성과 그로스 해킹 전략의 활용",
	            "content": [
	              "이벤트, 혜택 등을 시즌별로 전략적으로 활용하여 사용자 관심도를 높인 점이 돋보임",
	              "단기 퍼포먼스와 장기적 사용자 리텐션 두 측면을 균형 있게 고려함"
	            ]
	          },
	          {
	            "title": "실험과 결과 분석이 체계적임",
	            "content": [
	              "정량 지표를 기반으로 실험 결과를 비교 분석하여 가설을 검증한 방식이 체계적임",
	              "실험 설계에 있어서 명확한 기준이 있었고, 결과 해석도 논리적으로 설득력 있음"
	            ]
	          },
	          {
	            "title": "깔끔한 포트폴리오 구성",
	            "content": [
	              "카드 형식의 섹션 나눔과 정보 전달 구조가 명확함",
	              "강조 포인트(아이콘, 텍스트 강조 등)가 적절하게 사용되어 가독성이 높음"
	            ]
	          }
	        ],
	        "negativeFeedback": [
	          {
	            "title": "회고의 깊이 부족",
	            "content": [
	              "회고 항목이 다소 표면적인 내용에 그침",
	              "디자이너로서의 인사이트나 다음 프로젝트에 대한 연결점이 구체적으로 드러나지 않음"
	            ]
	          },
	          {
	            "title": "UI 개선 사례의 비교 시각화 부족",
	            "content": [
	              "리디자인 전/후의 명확한 비교 자료가 더 있었으면 효과가 더욱 명확했을 것",
	              "기존 문제를 해결한 UI 변화가 어떻게 반영되었는지 시각적 근거가 아쉬움"
	            ]
	          },
	          {
	            "title": "툴 및 협업 방식 설명 부족",
	            "content": [
	              "디자인 과정에서 어떤 툴을 썼는지, 협업 방식(Figma, Notion, Jira 등)에 대한 정보가 부족함",
	              "타 직군과의 협업 경험이 더 명확히 드러났으면 좋았을 것"
	            ]
	          },
	          {
	            "title": "사용자 인터뷰 등의 정성 데이터 부족",
	            "content": [
	              "정량 데이터는 잘 활용했으나, 정성적 피드백(사용자 인터뷰 등)이 보강되면 더 풍부한 분석이 가능했을 것",
	              "사용자의 실제 불편함이 어떻게 수집되고 반영되었는지에 대한 맥락이 약함"
	            ]
	          }
	        ],
	        "projectSummary": "문제 정의와 가설 설정부터 실험, 결과 분석까지 전체적으로 매우 탄탄한 구성의 프로젝트입니다. 특히 데이터 기반의 설계와 시즌성 전략 활용이 뛰어났으며, 포트폴리오 구성도 깔끔했습니다. 다만 회고와 비교 시각화, 협업 툴 활용 등은 조금 더 보완된다면 훨씬 더 완성도 높은 결과물이 될 것입니다."
	      }
	    ]
	  }
	  """;

  public static final String OverallSchema =
      """
		  	{
		  				"type": "object",
		  				"properties": {
		  				  "summary": {
		  					"type": "string",
		  					"description": "Overall evaluation summary"
		  				  },
		  				  "jobFit": {
		  					"type": "object",
		  					"properties": {
		  					  "score": {
		  						"type": "number",
		  						"description": "Job fit score"
		  					  },
		  					  "review": {
		  						"type": "string",
		  						"description": "Review of job fit"
		  					  }
		  					},
		  					"required": [
		  					  "score",
		  					  "review"
		  					],
		  					"additionalProperties": false
		  				  },
		  				  "logicalThinking": {
		  					"type": "object",
		  					"properties": {
		  					  "score": {
		  						"type": "number",
		  						"description": "Score for logical thinking"
		  					  },
		  					  "review": {
		  						"type": "string",
		  						"description": "Review of logical thinking"
		  					  }
		  					},
		  					"required": [
		  					  "score",
		  					  "review"
		  					],
		  					"additionalProperties": false
		  				  },
		  				  "writingClarity": {
		  					"type": "object",
		  					"properties": {
		  					  "score": {
		  						"type": "number",
		  						"description": "Score for writing clarity"
		  					  },
		  					  "review": {
		  						"type": "string",
		  						"description": "Review of writing clarity"
		  					  }
		  					},
		  					"required": [
		  					  "score",
		  					  "review"
		  					],
		  					"additionalProperties": false
		  				  },
		  				  "layoutReadability": {
		  					"type": "object",
		  					"properties": {
		  					  "score": {
		  						"type": "number",
		  						"description": "Score for layout readability"
		  					  },
		  					  "review": {
		  						"type": "string",
		  						"description": "Review of layout readability"
		  					  }
		  					},
		  					"required": [
		  					  "score",
		  					  "review"
		  					],
		  					"additionalProperties": false
		  				  },
		  				  "strengths": {
		  					"type": "array",
		  					"description": "List of strengths identified",
		  					"items": {
		  					  "type": "object",
		  					  "properties": {
		  						"title": {
		  						  "type": "string",
		  						  "description": "Title of strength"
		  						},
		  						"content": {
		  						  "type": "array",
		  						  "description": "Details about the strength",
		  						  "items": {
		  							"type": "string"
		  						  }
		  						}
		  					  },
		  					  "required": [
		  						"title",
		  						"content"
		  					  ],
		  					  "additionalProperties": false
		  					}
		  				  },
		  				  "improvements": {
		  					"type": "array",
		  					"description": "List of improvements suggested",
		  					"items": {
		  					  "type": "object",
		  					  "properties": {
		  						"title": {
		  						  "type": "string",
		  						  "description": "Title of improvement"
		  						},
		  						"content": {
		  						  "type": "array",
		  						  "description": "Details about the improvement",
		  						  "items": {
		  							"type": "string"
		  						  }
		  						}
		  					  },
		  					  "required": [
		  						"title",
		  						"content"
		  					  ],
		  					  "additionalProperties": false
		  					}
		  				  }
		  				},
		  				"required": [
		  				  "summary",
		  				  "jobFit",
		  				  "logicalThinking",
		  				  "writingClarity",
		  				  "layoutReadability",
		  				  "strengths",
		  				  "improvements"
		  				],
		  				"additionalProperties": false
		  			  }
			""";

  public static final String ProjectSchema =
      """
		  {
		         "name": "project_evaluation",
		         "strict": true,
		         "schema": {
		           "type": "object",
		           "properties": {
		             "projectEvaluation": {
		               "type": "array",
		               "description": "List of project evaluations.",
		               "items": {
		                 "type": "object",
		                 "properties": {
		                   "projectName": {
		                     "type": "string",
		                     "description": "프로젝트 이름"
		                   },
		                   "process": {
		                     "type": "array",
		                     "items": {
		                       "type": "string",
		                       "description": "각 프로세스별 평가입니다.",
		                       "enum": [
		                         "BAD",
		                         "SOSO",
		                         "GOOD"
		                       ]
		                     },
		                     "description": "프로젝트 진행 과정의 단계별 평가"
		                   },
		                   "processReview": {
		                     "type": "string",
		                     "description": "프로젝트 진행 과정에 대한 리뷰"
		                   },
		                   "positiveFeedback": {
		                     "type": "array",
		                     "items": {
		                       "type": "object",
		                       "properties": {
		                         "title": {
		                           "type": "string",
		                           "description": "긍정적이고 강점인 피드백 제목"
		                         },
		                         "content": {
		                           "type": "array",
		                           "items": {
		                             "type": "string",
		                             "description": "피드백 세부 내용"
		                           },
		                           "description": "긍정적이고 강점인 피드백 세부 내용"
		                         }
		                       },
		                       "required": [
		                         "title",
		                         "content"
		                       ],
		                       "additionalProperties": false
		                     },
		                     "description": "프로젝트내에서 강점인 부분"
		                   },
		                   "negativeFeedback": {
		                     "type": "array",
		                     "items": {
		                       "type": "object",
		                       "properties": {
		                         "title": {
		                           "type": "string",
		                           "description": "부정적인 피드백 제목"
		                         },
		                         "content": {
		                           "type": "array",
		                           "items": {
		                             "type": "string",
		                             "description": "피드백 세부 내용"
		                           },
		                           "description": "개선이 필요한 부분"
		                         }
		                       },
		                       "required": [
		                         "title",
		                         "content"
		                       ],
		                       "additionalProperties": false
		                     },
		                     "description": "개선이 필요한 부분"
		                   },
		                   "projectSummary": {
		                     "type": "string",
		                     "description": "프로젝트 전체 피드백 요약"
		                   }
		                 },
		                 "required": [
		                   "projectName",
		                   "process",
		                   "processReview",
		                   "positiveFeedback",
		                   "negativeFeedback",
		                   "projectSummary"
		                 ],
		                 "additionalProperties": false
		               }
		             }
		           },
		           "required": [
		             "projectEvaluation"
		           ],
		           "additionalProperties": false
		         }
		       }
		  """;
  public static final String PAGE_FEEDBACK_PROMPT =
      """
			[피드백 기준] -1. 번역체/어색한 표현 - 2. 문장이 길거나 가독성이 떨어지는 표현 - 3. 가독성 개선 - 4. 논리적 비약 - 5. 불필요한 반복 및 의미 명확화 / [작성 방식] - 각 페이지 번호를 기준으로 문제 있는 피드백 항목만 작성합니다. - 피드백 항목이 있는 페이지 수는 명확히 명시합니다. - 각 항목은 다음 형식으로 작성합니다: 페이지 번호: X / 카테고리: (1~5 중 하나) / 기존 문장: (문제 있는 원래 문장) / 수정 문장: (더 자연스럽고 명확한 문장) / 페이지 번호: X / 카테고리: 4 / 기존 문장: (문제 문장 전체 인용) / 수정 문장: (수정 문장) / 총 10개 정도의 ㅣ피드백을 해줘 / 데이터는 형식은 왼쪽이 페이지 번호, 오른쪽이 해당 페이지의 글자야. (2:안녕하세요.는 2페이지에 안녕하세요가 있는 거야.) [예시 출력] 페이지 번호: 3 / 카테고리: 1  기존 문장: 본 기능은 사용자에게 보다 직관적인 경험을 제공하고자 설계되었습니다.  수정 문장: 이 기능은 사용자가 더 쉽게 이해할 수 있도록 설계했습니다. 페이지 번호: 5 / 카테고리: 2 \s
			기존 문장: 사용자는 설정 페이지에서 알림 수신 여부를 직접 조정할 수 있는 기능을 사용할 수 있습니다.  수정 문장: 사용자는 설정 페이지에서 알림 수신 여부를 직접 설정할 수 있습니다.
			""";
  public static final String PAGE_FEEDBACK_SCHEMA =
      """
			{
			  "name": "page_feedback",
			  "strict": true,
			  "schema": {
			    "type": "object",
			    "properties": {
			      "피드백": {
			        "type": "array",
			        "description": "A collection of feedback entries.",
			        "items": {
			          "type": "object",
			          "properties": {
			            "페이지": {
			              "type": "number",
			              "description": "The page number related to the feedback."
			            },
			            "피드백_수": {
			              "type": "number",
			              "description": "The count of feedback entries for the page."
			            },
			            "항목": {
			              "type": "array",
			              "description": "A list of feedback items related to the page.",
			              "items": {
			                "type": "object",
			                "properties": {
			                  "카테고리": {
			                    "type": "number",
			                    "description": "The category of the feedback."
			                  },
			                  "기존_문장": {
			                    "type": "string",
			                    "description": "The original sentence that is being reviewed."
			                  },
			                  "수정_문장": {
			                    "type": "string",
			                    "description": "The suggested revised sentence."
			                  }
			                },
			                "required": [
			                  "카테고리",
			                  "기존_문장",
			                  "수정_문장"
			                ],
			                "additionalProperties": false
			              }
			            }
			          },
			          "required": [
			            "페이지",
			            "피드백_수",
			            "항목"
			          ],
			          "additionalProperties": false
			        }
			      }
			    },
			    "required": [
			      "피드백"
			    ],
			    "additionalProperties": false
			  }
			}
			""";
}

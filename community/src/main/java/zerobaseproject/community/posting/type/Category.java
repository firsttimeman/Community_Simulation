package zerobaseproject.community.posting.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Category {

    ALL("자유주제"),
    COMPANY("회사관련"),
    TECH("기술/IT"),
    FINANCE("재테크/금융"),
    CAREER("커리어/경력"),
    WORK_LIFE("업무생활"),
    LIFE("일상/취미"),
    EDUCATION("교육/자기계발"),
    SOCIAL("사회/이슈"),
    HEALTH("건강/운동"),
    ANONYMOUS("익명질문");

    private final String description;
}

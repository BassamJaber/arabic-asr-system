package demo.sphinx.helloworld;

public class Questions {

	private String Question;
	private String QuestionClass;
	private int ClassNum;
	
	public Questions() {
		super();
	}
	
	public Questions(String question , String questionClass){
		setQuestion(question);
		setQuestionClass(questionClass);
		switch(question){
		case "Œœ„« ":setClassNum(1);
					break;
		case "›Ê« »—":setClassNum(2);
					break;
		case "«Œ—Ï":setClassNum(3);
					break;
		}
	}
	
	public String getQuestion() {
		return Question;
	}
	public void setQuestion(String question) {
		Question = question;
	}
	
	public String getQuestionClass() {
		return QuestionClass;
	}
	public void setQuestionClass(String questionClass) {
		QuestionClass = questionClass;
	}
	

	public int getClassNum() {
		return ClassNum;
	}

	public void setClassNum(int classNum) {
		ClassNum = classNum;
	}

	@Override
	public String toString() {
		return "\"" + Question + "\","+ QuestionClass + "\n";
	}
	
	public String toString(boolean isIntClass){
		return ClassNum+",\'"+Question+"\'\n";
	}
	
	
}

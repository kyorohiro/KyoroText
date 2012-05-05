package info.kyorohiro.helloworld.pdfparser;

import java.util.ArrayList;
import java.util.LinkedList;

//thread unsafe.
public class PdfParser extends LookaheadParser {

	// work cash in any methodÅB
	// this field is only used by PdfObjectCreator's class.
	public Stack mCashForWork = new Stack();

	public PdfParser(Lexer lexer) {
		super(lexer);
	}

	public static class Stack {
		private LinkedList<Integer> mark = new LinkedList<Integer>();
		private LinkedList<Token>token = new LinkedList<Token>();

		public void mark() {
			int index = token.size()-1;
//			if(index<0){
//				index = 0;
//			}
			mark.addLast(index);
		}

		public int numOfMark(){
			return mark.size();
		}

		public int numOfToken(){
			return token.size();
		}

		public void release() {
			int num = mark.removeLast();
			while((num+1)<mark.size()&& mark.size()!=0){
				pop();
			}
		}

		public boolean isMarked() {
			int num = 0;
			if(0<mark.size()){
				num = mark.getLast();
			}
//			if((num+1)<=token.size()){
			if((num+1)<token.size()){
				return true;
			} else {
				return false;
			}
		}


		public void push(Token t) {
			token.addLast(t);
		}

		public Token pop() {
			return token.removeLast();
		}

		public Token peek() {
			return token.getLast();
		}
		
		public void testPrint(){
			for(int i=0;i<token.size();i++){
				android.util.Log.v("pdfparser","t["+i+"]="+token.get(i).mType);
				android.util.Log.v("pdfparser","t["+i+"]="+token.get(i).mText);
				android.util.Log.v("pdfparser","t["+i+"]="+token.get(i).mPattern);
				android.util.Log.v("pdfparser","t["+i+"]="+token.get(i).mValue);
			}

			for(int i=0;i<mark.size();i++){
				android.util.Log.v("pdfparser","m["+i+"]"+mark.get(i));
			}
			
		}
	}

}

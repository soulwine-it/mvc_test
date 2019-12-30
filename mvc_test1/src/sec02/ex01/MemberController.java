package sec02.ex01;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/member/*")
public class MemberController extends HttpServlet {
	MemberDAO memberDAO;
	
	public void init() throws ServletException{
		memberDAO = new MemberDAO();
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException{
		doHandle(request, response);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException{
		doHandle(request, response);
	}
	public void doHandle(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException{
		String nextPage = null;
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		//URL에서 요청명을 가져옵니다.
		String action = request.getPathInfo();
		System.out.println("action:"+action);
		if(action ==null ||action.equals("/listMembers.do")) {
			List membersList = memberDAO.listMembers();
			request.setAttribute("membersList", membersList);
			//test02 폴더의 listMember.jsp로 포워딩 합니다.
			nextPage = "/test02/listMembers.jsp";
		} 
			//action 값이 /addMember.do면 전송된 회원 정보를 가져와서 테이블에 추가합니다.
		else if(action.equals("/addMember.do")) {
			String id=request.getParameter("id");
			String pwd = request.getParameter("pwd");
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			MemberVO memberVO = new MemberVO(id, pwd, name, email);
			memberDAO.addMember(memberVO);
			//회원 등록 후 다시 회원 목록을 추가합니다.
			nextPage = "/member/listMembers.do";
			//action값이 /memberForm.do면 회원 가입창을 화면에 출력합니다.
		} else if (action.equals("/memberForm.do")) {
			nextPage = "/test02/memberForm.jsp";
			
		} else {
			List membersList = memberDAO.listMembers();
			request.setAttribute("membersList",  membersList);
			nextPage = "/test02/listMembers.jsp";
		}
		//nextPage에 지정한 요청명으로 다시 서블릿에 요청합니다.
		RequestDispatcher dispatch = request.getRequestDispatcher(nextPage);
		dispatch.forward(request, response);
	}
}

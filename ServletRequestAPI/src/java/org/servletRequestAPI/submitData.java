/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.servletRequestAPI;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author krzysztof
 */
public class submitData extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {

            out.println(request.getParameter("name"));
            out.println(request.getAttribute("name"));

            
            Enumeration en = request.getParameterNames();

            while(en.hasMoreElements())
                out.println(en.nextElement());


            Map vals = request.getParameterMap();

            for(Object v : vals.keySet())
                out.println( ((String[])vals.get(v))[0]);


            Enumeration<String> headers = request.getHeaderNames();

            out.println("<BR>");

            while(headers.hasMoreElements()){
                String header = headers.nextElement();
                out.println(header);

                out.println(request.getHeader(header));


                out.println("<BR>");

            }

out.println("<BR>");

Cookie[] cookies = request.getCookies();

for(Cookie c : cookies){
    out.println(c);
    out.println(c.getName());
    out.println(c.getMaxAge());
    out.println("<BR>");
    out.println(c.getValue());
}




        } finally { 
            out.close();
        }
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}

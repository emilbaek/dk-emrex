package dk.kmd.emrex.smpmock;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ThymeController {
	@Value("${server.port}")
	private String serverport; 
	
	@Value("${environment.url}")
	private String environmentUrl;
	
	@Value("${environment.port}")
	private String environmentPort; 
	 
	@Value("${return.url}")
	private String returnUrl;
	
	private byte[] pdfBytes;
	
	private byte[] xmlBytes;
	
	@RequestMapping("/")
	public String indexPage(){
		return "index"; 
	}
	@RequestMapping("/greeting")
	public String greeting(@RequestParam(value = "name", required = false, defaultValue = "world") String name,
			Model model) {
		model.addAttribute("name", name);
		return "greeting";
	}
	@RequestMapping("/smp")
	public String ncp(Model model){
		model.addAttribute("serverport", this.serverport);
		model.addAttribute("returnUrl", this.returnUrl); 
		model.addAttribute("environmentUrl", this.environmentUrl);
		model.addAttribute("environmentPort", this.environmentPort);
		return "smp";
	}
	@RequestMapping(value="/onReturn", method=RequestMethod.POST)
	public String onReturn(@ModelAttribute("returnData") NCPReturnData returnData, Model model){
		String base64encodedElmo = returnData.getElmo(); 
		try {
			this.xmlBytes = Helper.uncompressGzip(Helper.decode64(base64encodedElmo));
			this.pdfBytes = Helper.getPdfFromElmoXml(this.xmlBytes); 
		} catch (IOException e) {
			model.addAttribute("elmo", e); 
		} 
		model.addAttribute("returnCode", returnData.getReturnCode());
		model.addAttribute("sesisonId", returnData.getSessionId());
		return "result"; 
	}
	
	@RequestMapping(value="/getPdf", method=RequestMethod.GET)
	public @ResponseBody void getPDF(HttpServletRequest request, HttpServletResponse response){
		response.reset();
		response.setBufferSize(this.pdfBytes.length);
		response.setContentType("application/pdf");
		try {
			response.getOutputStream().write(this.pdfBytes);
		} catch (IOException e) {
			log.error("Error sending PDF to user", e);
		}
	}
	
	@RequestMapping(value="/getXml", method=RequestMethod.GET)
	public @ResponseBody void getXML(HttpServletRequest request, HttpServletResponse response){
		response.reset();
		response.setBufferSize(this.xmlBytes.length);
		response.setContentType("application/xml");
		try {
			response.getOutputStream().write(this.xmlBytes);
		} catch (IOException e) {
			log.error("Error sending XML to user", e);
		}
	}
	
}

package onlineShop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import onlineShop.model.Customer;
import onlineShop.service.CustomerService;

@Controller
@SessionAttributes("customer")
public class RegistrationController {
	
	@Autowired
	private CustomerService customerService;

	@RequestMapping(value = "/customer/registration", method = RequestMethod.GET)
	public ModelAndView getRegistrationForm() {
		Customer customer = new Customer();
		return new ModelAndView("register", "customer", customer);
		//register�����û������ĵ�һ��ҳ��������register.jsp
		//�û���ÿ�δ�������Ҫnewһ���µĶ���Ӷ����ղ�ͬ����Ϣ�����Դ�ǰ�˴�������customer�������,
		//����MVC�İ󶨻���ͨ��.jsp�ļ��е�path��Ӧ��customer��������ĳ�Ա����
	}

	@RequestMapping(value = "/customer/registration", method = RequestMethod.POST)
	public ModelAndView registerCustomer(@ModelAttribute(value = "customer") Customer customer,
			BindingResult result) {
		ModelAndView modelAndView = new ModelAndView();
		if (result.hasErrors()) {//check�󶨵Ĺ����Ƿ���error
			modelAndView.setViewName("register"); //setViewName����ת�������.jspҳ��
			return modelAndView;
		}
		customerService.addCustomer(customer);
		modelAndView.setViewName("login");
		modelAndView.addObject("registrationSuccess", "Registered Successfully. Login using username and password");
		return modelAndView;
	}
}

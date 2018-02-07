package mdb;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import ejbProject.Income;
import ejbProject.IncomeDAO;

@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/jms/queue/test"),
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge") })

public class IncomeServiceMDB implements MessageListener {

	@EJB
	private IncomeDAO stub;

	@Override
	public void onMessage(Message message) {
		try {
			if (message instanceof ObjectMessage) {
				Object obj = ((ObjectMessage) message).getObject();
				if (obj instanceof Income) {
					Income theIncome = (Income) obj;
					stub.storeIncome(theIncome);
				} else {
					throw new Exception("Illegal message content");
				}

			} else {
				throw new Exception("Illegal message type");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

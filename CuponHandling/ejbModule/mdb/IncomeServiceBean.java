package mdb;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;

import ejbProject.Income;

@Stateless
public class IncomeServiceBean implements IncomeService {

	// server injects factory to this attribute
	@Resource(lookup = "java:jboss/DefaultJMSConnectionFactory")
	private QueueConnectionFactory factory;

	private QueueConnection connection;
	private QueueSession session;

	// server injects factory to this attribute
	@Resource(lookup = "java:/jms/queue/test")
	private Queue theQueue;

	private QueueSender producer;
	private ObjectMessage theMessage;
	//private TextMessage xmlMessage;

	public IncomeServiceBean() {
	}

	@Override
	public void StoreService(Income income) {
		try {
			// send first message - as ObjectMessage with serialized Income
			theMessage.setObject(income);
			producer.send(theMessage);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	@PostConstruct
	public void init() {
		try {
			// 1 lookup factory (performed by @Resource)
			// factory = (TopicConnectionFactory) new
			// InitialContext().lookup("java:jboss/DefaultJMSConnectionFactory");

			// 2 create Connection
			connection = factory.createQueueConnection();

			// 3 create session
			boolean txFlag = false;// do not support transaction
			session = connection.createQueueSession(txFlag, Session.AUTO_ACKNOWLEDGE);

			// 4 lookup destination (performed by @Resource)
			// theTopic = (Topic) new InitialContext().lookup("java:/jms/topic/test");

			// 5 create producer
			producer = session.createSender(theQueue);

			// 6 create message
			theMessage = session.createObjectMessage();
			connection.start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@PreDestroy
	public void destory() {
		try {
			connection.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			producer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

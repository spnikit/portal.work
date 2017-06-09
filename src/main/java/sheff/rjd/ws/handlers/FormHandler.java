package sheff.rjd.ws.handlers;
import org.apache.log4j.Logger;

/**
 * Created by xblx on 05.05.15.
 *
 * Описывает общий вид некого обработчика для форм.
 * При создании реализации даннного интерфейса указывайте тип вашего обработчика
 * из констант, определенных в данном интерфейсе.
 *
 * После создания вашего обработчика добавьте его в formControllers.xml к нужному контроллеру.
 */
public interface FormHandler
{
	static Logger log	= Logger.getLogger(FormHandler.class);
	
	public static final int XML_GENERATOR = 1;

	public Object handle(Object... params) throws Exception;

	public int getType();
}

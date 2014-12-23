package priceboard.event.server.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import priceboard.event.EventHandler;

@Component
public class EventHandlerFilter {
	
	public List<EventHandler> filter(List<EventHandler> handlers, List<String> types) {
		List<EventHandler> returnHandlers = new ArrayList<EventHandler>(handlers.size());
		int minPriorityValue = Integer.MAX_VALUE;
		int maxPriorityValue = -1;
		Map<Integer, List<EventHandler>> mapPriorityHandler = new HashMap<Integer, List<EventHandler>>();
		for (EventHandler handler : handlers) {
			EventHandlerApplyFor annotation = handler.getClass().getAnnotation(EventHandlerApplyFor.class);
			if (annotation == null) {
				continue;
			}
			String[] values = annotation.values();
			if (values == null) {
				continue;
			}
			
			for (String value : values) {
				if (types.contains(value)) {
					int currentPriority = annotation.priority();
					
					if (minPriorityValue > currentPriority) {
						minPriorityValue = currentPriority;
					}
					
					if (maxPriorityValue < currentPriority) {
						maxPriorityValue = currentPriority;
					}
					
					updateHandlerWithPriorityInMap(currentPriority, handler, mapPriorityHandler);
					
				}
			}
		}
		
		sortHandlerByPriority(returnHandlers, mapPriorityHandler);
		return returnHandlers;
	}
	
	private void updateHandlerWithPriorityInMap(int currentPriority, EventHandler handler, Map<Integer, List<EventHandler>> mapPriorityHandler) {
		List<EventHandler> handlersWithCurrentPriority = mapPriorityHandler.get(currentPriority);
		if (handlersWithCurrentPriority == null) {
			handlersWithCurrentPriority = new ArrayList<EventHandler>();
			mapPriorityHandler.put(currentPriority, handlersWithCurrentPriority);
		}
		handlersWithCurrentPriority.add(handler);
	}

	private void sortHandlerByPriority(List<EventHandler> returnHandlers, Map<Integer, List<EventHandler>> mapPriorityHandler) {
		for(Map.Entry<Integer, List<EventHandler>> entry : mapPriorityHandler.entrySet()) {
			List<EventHandler> handlersWithCurrentPriority = entry.getValue();
			if (handlersWithCurrentPriority != null) {
				returnHandlers.addAll(handlersWithCurrentPriority);
			}
		}	
	}

}



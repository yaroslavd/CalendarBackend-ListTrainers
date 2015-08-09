package personal.dvinov.calendar.service.listtrainers.api;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * POJO to return to the caller
 */
public class ListTrainersResponse {
	public static class Trainer {
	    public final String id;
	    public final String name;
	    
	    public Trainer(final String id, final String name) {
	        this.id = id;
	        this.name = name;
	    }
	    
	    @Override
	    public String toString() {
	        return new ToStringBuilder(this)
	                .append(id)
	                .append(name)
	        .build();
	    }
	}
	
	public final List<Trainer> trainers;
	
	public ListTrainersResponse(final List<Trainer> trainers) {
	    this.trainers = trainers;
	}
	
	@Override
	public String toString() {
        return new ToStringBuilder(this)
                .append(trainers)
        .build();
	}
}

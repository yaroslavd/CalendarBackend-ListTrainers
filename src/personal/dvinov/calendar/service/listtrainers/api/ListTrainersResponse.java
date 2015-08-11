package personal.dvinov.calendar.service.listtrainers.api;

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ListTrainersResponse {
	public static class Trainer {
	    private String id;
	    private String name;
	    
	    public Trainer(final String id, final String name) {
	        this.id = id;
	        this.name = name;
	    }
	    
	    public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
	    public String toString() {
	        return new ToStringBuilder(this)
	                .append(id)
	                .append(name)
	        .build();
	    }
        
        @Override
        public boolean equals(final Object obj) {
            if (obj == null) {
                return false;
            }
            
            if (obj == this) {
                return true;
            }
            
            if (obj.getClass() != getClass()) {
                return false;
            }
            
            Trainer other = (Trainer) obj;
            
            return new EqualsBuilder()
                    .append(getId(), other.getId())
                    .append(getName(), other.getName())
            .build();
        }
	}
	
	private List<Trainer> trainers;
	
	public ListTrainersResponse(final List<Trainer> trainers) {
	    this.trainers = trainers;
	}
	
	public List<Trainer> getTrainers() {
        return trainers;
    }

    public void setTrainers(List<Trainer> trainers) {
        this.trainers = trainers;
    }

    @Override
	public String toString() {
        return new ToStringBuilder(this)
                .append(trainers)
        .build();
	}
    
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        
        if (obj == this) {
            return true;
        }
        
        if (obj.getClass() != getClass()) {
            return false;
        }
        
        ListTrainersResponse other = (ListTrainersResponse) obj;
        
        return new EqualsBuilder()
                .append(getTrainers(), other.getTrainers())
        .build();
    }
}

package es.unizar.smartcampuz.model.report;

import es.unizar.smartcampuz.model.worker.Worker;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "report")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String roomID;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "worker_id")
    private Worker worker;

    private String description;

    @Enumerated(EnumType.STRING)
    private ReportState state = ReportState.INBOX;

//    @Column
//    @Type(type="timestamp")
//    private Date createDate;
//
//    @Column
//    @Type(type="timestamp")
//    private Date modifyDate;

    public Report(){ }

    public Report(long id) {
        this.id = id;
    }

    public Report(@NotNull String roomID, Worker worker, @NotNull String description,
                  @NotNull ReportState state){
        this.roomID=roomID;
        this.worker=worker;
        this.description = description;
        this.state=state;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setState(ReportState state) {
        this.state = state;
    }

    public ReportState getState() {
        return state;
    }
//
//    public void setCreateDate(Date createDate) {
//        this.createDate = createDate;
//    }
//
//    public Date getCreateDate() {
//        return createDate;
//    }
//
//    public void setModifyDate(Date modifyDate) {
//        this.modifyDate = modifyDate;
//    }
//
//    public Date getModifyDate() {
//        return modifyDate;
//    }
}

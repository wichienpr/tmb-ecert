import { Component, OnInit } from '@angular/core';
import { Calendar, CalendarFormatter, CalendarLocal, CalendarType, Lov, Modal } from 'app/baiwa/common/models';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Btm01000Service } from 'app/tmb-ecert/btm00000/btm01000/btm01000.service';
declare var $: any;
@Component({
  selector: 'app-btm01000',
  templateUrl: './btm01000.component.html',
  styleUrls: ['./btm01000.component.css']
})
export class Btm01000Component implements OnInit {
  isShowResult:boolean;
  calendar1: Calendar;
  calendar2: Calendar;
  serchForm: FormGroup;

  dropdownBatchJob: any;
  dropdownJobMonitor: any;
  requestObj:any;
  responseObj:any;
  detailMSG:String;

  detailModal: Modal = {
    modalId: "detailModalId",
    type: "custom"
  };

  retrunModal: Modal = {
    modalId: "retrunModalId",
    type: "custom"
  };

  constructor(private service: Btm01000Service) {


    this.serchForm = new FormGroup({
      dateForm: new FormControl(null, Validators.required),
      dateTo: new FormControl(null, Validators.required),
      batchType: new FormControl(null, Validators.required),
      operationType: new FormControl(null, Validators.required),
    });


    this.calendar1 = {
      calendarId: "cal1",
      calendarName: "cal1",
      formGroup: this.serchForm,
      formControlName: "dateForm",
      type: CalendarType.DATE,
      formatter: CalendarFormatter.DEFAULT,
      local: CalendarLocal.EN,
      icon: "time icon",
      endId: "cal2",
      initial: new Date
    };
    this.calendar2 = {
      calendarId: "cal2",
      calendarName: "cal2",
      formGroup: this.serchForm,
      formControlName: "dateTo",
      type: CalendarType.DATE,
      formatter: CalendarFormatter.DEFAULT,
      local: CalendarLocal.EN,
      icon: "time icon",
      startId: "cal1",
      initial: new Date
    };

    this.dropdownBatchJob = {
      action: {
        dropdownId: "batchJobType",
        dropdownName: "batchJobType",
        type: "search",
        formGroup: this.serchForm,
        formControlName: "batchType",
        values: [],
        valueName: "code",
        labelName: "name"
      }
    };

    this.dropdownJobMonitor = {
      action: {
        dropdownId: "jobMonitoring",
        dropdownName: "jobMonitoring",
        type: "search",
        formGroup: this.serchForm,
        formControlName: "operationType",
        values: [],
        valueName: "code",
        labelName: "name"
      }
    };
    this.requestObj = {
      dateFrom:'',
      dateTo:'',
      batchJobType:'',
      batchStatus:''
    }
    this.responseObj =[{}];

    this.isShowResult = false;
    this.detailMSG ='';

    this.service.getDropdownBatchJob().subscribe((obj: Lov[]) => this.dropdownBatchJob.action.values = obj);
    this.service.getDropdownJobMonitoringStatus().subscribe((obj: Lov[]) => this.dropdownJobMonitor.action.values = obj);

  }

  ngOnInit() {
  }

  calendarValue(name, e) {
    this.serchForm.controls[name].setValue(e);
    console.log(this.serchForm);
    console.log(this.serchForm.controls[name].value);
  }

  doSearch(){
    // console.log("dateTo : ",  this.serchForm.value.dateTo);
    // console.log("dateForm : ",  this.serchForm.value.dateForm);
    this.isShowResult = true;
    this.requestObj.dateFrom = this.serchForm.value.dateForm;
    this.requestObj.dateTo = this.serchForm.value.dateTo;
    this.requestObj.batchJobType = this.serchForm.value.batchType;
    this.requestObj.batchStatus = this.serchForm.value.operationType;
    this.service.getListBatch(this.requestObj).subscribe(res=>{
      this.responseObj = res;
      console.log("res =>",res );
    },error=>{

    });

  }
  showModalDetail(index) {
    this.detailMSG = this.responseObj[index].errorDesc
    $('#detailModalId').modal('show');
  }

  showModalReturn() {

    $('#retrunModalId').modal('show');
  }
  closeModal(){
    $('#detailModalId').modal('hide');
    $('#retrunModalId').modal('hide');
  }


}

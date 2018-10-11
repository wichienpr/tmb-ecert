import { Component, OnInit, ViewChild } from '@angular/core';
import { Calendar, CalendarFormatter, CalendarLocal, CalendarType, Lov, Modal } from 'app/baiwa/common/models';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Btm01000Service } from 'app/tmb-ecert/btm00000/btm01000/btm01000.service';
import { DatatableCofnig, DatatableDirective } from 'app/baiwa/common/directives/datatable/datatable.directive';
declare var $: any;
@Component({
  selector: 'app-btm01000',
  templateUrl: './btm01000.component.html',
  styleUrls: ['./btm01000.component.css']
})
export class Btm01000Component implements OnInit {

  @ViewChild("batchMonitorDT")
  batchMonitorDT: DatatableDirective
  
  isShowResult:boolean;
  calendar1: Calendar;
  calendar2: Calendar;
  calendarReturnFrom:Calendar;
  calendarReturnTo:Calendar;
  serchForm: FormGroup;
  returnForm :FormGroup;

  dropdownBatchJob: any;
  dropdownJobMonitor: any;
  requestObj:any;
  responseObj:any;
  detailMSG:String;

  touchedReturn:boolean;

  batchMonitorConfig:DatatableCofnig;
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
      batchType: new FormControl(null),
      operationType: new FormControl(null),
    });

    this.returnForm = new FormGroup({
      returnDateForm: new FormControl(null, Validators.required),
      returnDateTo: new FormControl(null, Validators.required),
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
      // initial: new Date
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
      // initial: new Date
    };

    this.calendarReturnFrom = {
      calendarId: "returnForm",
      calendarName: "returnForm",
      formGroup: this.returnForm,
      formControlName: "returnDateForm",
      type: CalendarType.DATE,
      formatter: CalendarFormatter.DEFAULT,
      local: CalendarLocal.EN,
      icon: "time icon",
      endId: "returnTo",
      // initial: new Date
    };
    this.calendarReturnTo = {
      calendarId: "returnTo",
      calendarName: "returnTo",
      formGroup: this.returnForm,
      formControlName: "returnDateTo",
      type: CalendarType.DATE,
      formatter: CalendarFormatter.DEFAULT,
      local: CalendarLocal.EN,
      icon: "time icon",
      startId: "returnForm",
      // initial: new Date
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
    this.touchedReturn = false;

    this.service.getDropdownBatchJob().subscribe((obj: Lov[]) => this.dropdownBatchJob.action.values = obj);
    this.service.getDropdownJobMonitoringStatus().subscribe((obj: Lov[]) => this.dropdownJobMonitor.action.values = obj);

  }

  ngOnInit() {

    this.batchMonitorConfig = {
      url: "/api/btm/btm01000/getListBatch2",
      serverSide: false,
      useBlockUi : true
    };
  }

  calendarValue(name, e) {
    this.serchForm.controls[name].setValue(e);
    console.log(this.serchForm);
    console.log(this.serchForm.controls[name].value);
  }

  calendarReturnValue(name, e) {
    this.returnForm.controls[name].setValue(e);
    console.log(this.returnForm);
    console.log(this.returnForm.controls[name].value);
  }

  doSearch(){
    // Object.keys(this.serchForm.controls).forEach(controlName => this.serchForm.controls[controlName].markAsTouched());
    if(!this.serchForm.touched){
      Object.keys(this.serchForm.value).forEach(element => {
        let fc = this.serchForm.get(element);
        fc.markAsTouched({onlySelf : true});
      });
    }
    // console.log("dateTo : ", this.serchForm.valid);
    // console.log("dateForm : ",  this.serchForm.value.dateForm);
    if (this.serchForm.valid){
      this.isShowResult = true;
      this.requestObj.dateFrom = this.serchForm.value.dateForm;
      this.requestObj.dateTo = this.serchForm.value.dateTo;
      this.requestObj.batchJobType = this.serchForm.value.batchType;
      this.requestObj.batchStatus = this.serchForm.value.operationType;
      // this.service.getListBatch(this.requestObj).subscribe(res=>{
      //   this.responseObj = res;
      //   // console.log("res =>",res );
      // },error=>{
  
      // });

      this.batchMonitorDT.search()
    }


  }
  actionChange(){

  }
  showModalDetail(index) {
    this.detailMSG = this.responseObj[index].errorDesc
    $('#detailModalId').modal('show');
  }

  showModalReturn() {
    this.touchedReturn = true;
    $('#retrunModalId').modal('show');
  }
  closeModal(){
    $('#detailModalId').modal('hide');
    $('#retrunModalId').modal('hide');
  }

  clearSearchData(){
    // console.log("dateTo : ",  this.serchForm.touched);
    // var initial = new Date();
    $('#cal1').calendar('set date', '');
    $('#cal2').calendar('set date', '');
    this.calendarReturnFrom
  }

  get dateForm (){
    return this.serchForm.get("dateForm");
  }
  get dateTo (){
    return this.serchForm.get("dateTo");
  }
}

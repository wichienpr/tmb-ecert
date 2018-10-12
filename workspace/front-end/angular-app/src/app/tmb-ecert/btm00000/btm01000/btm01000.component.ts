import { Component, OnInit, ViewChild } from '@angular/core';
import { Calendar, CalendarFormatter, CalendarLocal, CalendarType, Lov, Modal } from 'app/baiwa/common/models';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Btm01000Service } from 'app/tmb-ecert/btm00000/btm01000/btm01000.service';
import { DatatableCofnig, DatatableDirective } from 'app/baiwa/common/directives/datatable/datatable.directive';
import { ModalService, CommonService } from 'app/baiwa/common/services';
import { ModalComponent } from 'app/baiwa/common/components/modal/modal.component';
import { MESSAGE_STATUS, PAGE_AUTH } from 'app/baiwa/common/constants';
import { NgCalendarConfig, NgCalendarComponent } from 'app/baiwa/common/components/calendar/ng-calendar.component';
import { Btm01000 } from './btm01000.model';
@Component({
  selector: 'app-btm01000',
  templateUrl: './btm01000.component.html',
  styleUrls: ['./btm01000.component.css']
})
export class Btm01000Component implements OnInit {

  @ViewChild("batchMonitorDT")
  batchMonitorDT: DatatableDirective;

  @ViewChild("modalreRun")
  modalreRun: ModalComponent;

  @ViewChild("calendarRerunFrom")
  calendarRerunFrom: NgCalendarComponent;

  @ViewChild("calendarRerunTo")
  calendarRerunTo: NgCalendarComponent;


  isShowResult: boolean;
  calendar1: Calendar;
  calendar2: Calendar;
  calendarreRunFrom: NgCalendarConfig;
  calendarreRunTo: NgCalendarConfig;
  serchForm: FormGroup;
  reRunForm: FormGroup;

  dropdownBatchJob: any;
  dropdownJobMonitor: any;
  requestObj: any;
  messageRes: any;
  detailMSG: String;
  touchedreRun: boolean;

  batchMonitorConfig: DatatableCofnig;
  rerunDateOnly: boolean = true;
  retrunModal: Modal;

  tempItem: Btm01000;

  constructor(private service: Btm01000Service, private modalService: ModalService, private commonserice: CommonService) {


    this.serchForm = new FormGroup({
      dateFrom: new FormControl(null, Validators.required),
      dateTo: new FormControl(null, Validators.required),
      batchType: new FormControl(null),
      operationType: new FormControl(null),
    });

    this.reRunForm = new FormGroup({
      reRunDateForm: new FormControl(null, Validators.required),
      reRunDateTo: new FormControl(null, Validators.required),
    });


    this.calendar1 = {
      calendarId: "cal1",
      calendarName: "cal1",
      formGroup: this.serchForm,
      formControlName: "dateFrom",
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
    this.messageRes = {
      data: "",
      message: ""
    }
    this.rerunDateOnly = true;
  }

  ngOnInit() {
    this.touchedreRun = false;

    this.retrunModal = {
      modalId: "retrunModalId",
      type: "custom"
    };

    this.service.getDropdownBatchJob().subscribe((obj: Lov[]) => this.dropdownBatchJob.action.values = obj);
    this.service.getDropdownJobMonitoringStatus().subscribe((obj: Lov[]) => this.dropdownJobMonitor.action.values = obj);

    this.batchMonitorConfig = {
      url: "/api/btm/btm01000/getListBatch2",
      serverSide: true,
      useBlockUi: true
    };

    this.calendarreRunFrom = {
      id: "reRunDateForm",
      formControl: this.reRunForm.get("reRunDateForm"),
      endCalendar: "reRunDateTo",
      formatter: "dd/mm/yyyy"
    };
    this.calendarreRunTo = {
      id: "reRunDateTo",
      formControl: this.reRunForm.get("reRunDateTo"),
      endCalendar: "reRunDateForm",
      formatter: "dd/mm/yyyy"
    };
  }

  calendarValue(name, e) {
    this.serchForm.controls[name].setValue(e);
  }

  // calendarreRunValue(name, e) {
  //   this.reRunForm.controls[name].setValue(e);
  // }

  doSearch() {
    if (!this.serchForm.touched) {
      Object.keys(this.serchForm.value).forEach(element => {
        let fc = this.serchForm.get(element);
        fc.markAsTouched({ onlySelf: true });
      });
    }
    if (this.serchForm.valid) {
      this.batchMonitorDT.searchParams(this.serchForm.value)
      this.batchMonitorDT.search()
    }
  }
  actionChange() {

  }
  showModalDetail(item) {
    let modal: Modal = {
      msg: item.errorDesc
    }
    this.modalService.alert(modal);
  }

  showModalreRun(item) {
    let dateF: string = item.endofdate;
    this.touchedreRun = true;
    console.log("date format", dateF.substr(0, 10))
    if (item.jobtypeCode == 60003 || item.jobtypeCode == 60002 || item.jobtypeCode == 60001) {
      if (item.jobtypeCode != 60003) {
        this.rerunDateOnly = false;
        this.reRunForm.get("reRunDateForm").patchValue(dateF.substr(0, 10));
      } else {
        this.rerunDateOnly = true;
      }
      this.tempItem = item;
      this.modalreRun.showModal();
    } else {
      const modalConf: Modal = {
        msg: `<label>ท่านต้องการ Rerun Job หรือไม่</label>`,
        title: "ยืนยันการ Rerun",
        color: "notification"
      }
      this.modalService.confirm((e) => {
        if (e) {
          this.callRerunJobAPI(item);
          console.log("comfirm modal ");
        }
      }, modalConf);
    }
  }

  closeModal() {
    this.modalreRun.closeModal();
  }

  clearSearchData() {
    this.serchForm.reset();
    this.batchMonitorDT.clear();
  }
  continueRerun() {
    this.tempItem.endofdate = this.reRunForm.get("reRunDateForm").value
    this.callRerunJobAPI(this.tempItem)

  }
  callRerunJobAPI(itme) {
    this.service.callRerunJobService(itme).subscribe(res => {
      this.messageRes = res;
      if (this.messageRes.message == MESSAGE_STATUS.SUCCEED) {
        // this.modalService.alert(this.messageRes.message);
        this.modalreRun.closeModal();
      } else {
        this.modalService.alert(this.messageRes.message);
      }
    }, error => {

    }, () => {
      // this.tempItem = null;
    })
  }

  modalEvent(event: any) {
    console.log("modal event : ", event);
    this.calendarRerunFrom.refresh();
    this.calendarRerunTo.refresh();
  }

  get dateFrom() {
    return this.serchForm.get("dateFrom");
  }
  get dateTo() {
    return this.serchForm.get("dateTo");
  }
  get btnrerun() {
    return this.commonserice.isAuth(PAGE_AUTH.P0001101)
  }

}

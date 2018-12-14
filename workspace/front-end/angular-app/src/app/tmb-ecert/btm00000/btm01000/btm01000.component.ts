import { Component, OnInit, ViewChild, AfterViewInit, OnDestroy } from '@angular/core';
import { Calendar, Lov, Modal } from 'app/baiwa/common/models';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Btm01000Service } from 'app/tmb-ecert/btm00000/btm01000/btm01000.service';
import { DatatableCofnig, DatatableDirective } from 'app/baiwa/common/directives/datatable/datatable.directive';
import { ModalService, CommonService } from 'app/baiwa/common/services';
import { ModalComponent } from 'app/baiwa/common/components/modal/modal.component';
import { MESSAGE_STATUS, PAGE_AUTH } from 'app/baiwa/common/constants';
import { NgCalendarConfig, NgCalendarComponent } from 'app/baiwa/common/components/calendar/ng-calendar.component';
import { Btm01000 } from './btm01000.model';
import * as moment from 'moment';
import { DropdownComponent } from 'app/baiwa/common/components/dropdown/dropdown.component';
import { ThDateToEnDate, EnDateToThDate } from "helpers/";

@Component({
  selector: 'app-btm01000',
  templateUrl: './btm01000.component.html',
  styleUrls: ['./btm01000.component.css']
})
export class Btm01000Component implements OnInit, AfterViewInit, OnDestroy {

  @ViewChild("batchMonitorDT")
  batchMonitorDT: DatatableDirective;

  @ViewChild("modalreRun")
  modalreRun: ModalComponent;

  @ViewChild("calendarRerunFrom")
  calendarRerunFrom: NgCalendarComponent;

  @ViewChild("calendarRerunTo")
  calendarRerunTo: NgCalendarComponent;

  @ViewChild("calendarFrom")
  calendarFrom: NgCalendarComponent;

  @ViewChild("calendarTo")
  calendarTo: NgCalendarComponent;

  @ViewChild("statusDropDown")
  statusDropDown: DropdownComponent;

  @ViewChild("typeDropDown")
  typeDropDown: DropdownComponent;



  isShowResult: boolean;
  calendar1: Calendar;
  calendar2: Calendar;
  calendarreRunFrom: NgCalendarConfig;
  calendarreRunTo: NgCalendarConfig;

  calendarFromConig: NgCalendarConfig;
  calendarToConig: NgCalendarConfig;

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
  interval: any = null;
  timeInterval: number = 30;

  constructor(private service: Btm01000Service, private modalService: ModalService,
    private commonserice: CommonService) {

    this.messageRes = {
      data: "",
      message: ""
    }
    this.rerunDateOnly = true;

    this.service.getParameters().subscribe(response => {
      const data = response as Array<any>;
      const selected = data.find(obj => obj.propertyName === "time.to.interval");
      this.timeInterval = parseInt(selected ? selected.propertyValue : "30");
    });
  }

  ngOnInit() {
    let now = moment().format('DD/MM/YYYY');

    this.serchForm = new FormGroup({
      dateFrom: new FormControl(now, Validators.required),
      dateTo: new FormControl(now, Validators.required),
      batchType: new FormControl(),
      operationType: new FormControl(),
    });

    this.reRunForm = new FormGroup({
      reRunDateForm: new FormControl(null, Validators.required),
      reRunDateTo: new FormControl(null, Validators.required),
    });

    this.touchedreRun = false;
    this.retrunModal = {
      modalId: "retrunModalId",
      type: "custom"
    };

    this.dropdownBatchJob = {
      dropdownId: "batchJobType",
      dropdownName: "batchJobType",
      type: "search",
      formGroup: this.serchForm,
      formControlName: "batchType",
      values: [],
      valueName: "code",
      labelName: "name"
    };

    this.dropdownJobMonitor = {
      dropdownId: "jobMonitoring",
      dropdownName: "jobMonitoring",
      type: "search",
      formGroup: this.serchForm,
      formControlName: "operationType",
      values: [],
      valueName: "code",
      labelName: "name"

    };

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
      startCalendar: "reRunDateForm",
      formatter: "dd/mm/yyyy"
    };

    this.calendarFromConig = {
      id: "dateForm",
      formControl: this.serchForm.get("dateFrom"),
      endCalendar: "dateTo",
      formatter: "dd/mm/yyyy"
    };
    this.calendarToConig = {
      id: "dateTo",
      formControl: this.serchForm.get("dateTo"),
      startCalendar: "dateFrom",
      formatter: "dd/mm/yyyy"
    };

    this.service.getDropdownBatchJob().subscribe((obj: Lov[]) => {
      this.dropdownBatchJob.values = obj
    });
    this.service.getDropdownJobMonitoringStatus().subscribe((obj: Lov[]) => {
      this.dropdownJobMonitor.values = obj
    });
  }

  ngAfterViewInit() {
    // On Page Created and Searched
    setTimeout(() => {
      this.doSearch();
    }, 500);

    // Delay 3 seconds for loading parameter and find time to interval
    setTimeout(() => {
      // Refresh This Page
      this.interval = setInterval(() => {
        this.doSearch();
      }, this.timeInterval * 60 * 1000); // 30 minute refresh
      console.log(`INTERVAL: ${this.timeInterval} MINUTES`);
    }, 3000);
  }

  ngOnDestroy() {
    clearInterval(this.interval);
    console.log("CLEARED INTERVAL");
  }

  calendarValue(name, e) {
    this.serchForm.controls[name].setValue(e);
  }

  doSearch() {
    if (!this.serchForm.touched) {
      Object.keys(this.serchForm.value).forEach(element => {
        let fc = this.serchForm.get(element);
        fc.markAsTouched({ onlySelf: true });
      });
    }
    if (this.serchForm.valid) {
      let strFrom = this.serchForm.get("dateFrom").value;
      let strTo = this.serchForm.get("dateTo").value;
      let type = this.serchForm.get("batchType").value;
      let oper = this.serchForm.get("operationType").value
      const formsearch = {
        dateFrom: ThDateToEnDate(strFrom),
        dateTo: ThDateToEnDate(strTo),
        batchType: type,
        operationType: oper

      }
      this.batchMonitorDT.searchParams(formsearch)
      this.batchMonitorDT.search();
    }
  }

  showModalDetail(item) {
    let modal: Modal = {
      msg: item.errorDesc
    }
    this.modalService.alert(modal);
  }

  showModalreRun(item) {
    let btmTemp: Btm01000 = Object.assign({}, item);
    let dateF: string = btmTemp.endofdate;
    this.touchedreRun = true;
    if (item.jobtypeCode == 60003 || item.jobtypeCode == 60002 || item.jobtypeCode == 60001) {
      if (item.jobtypeCode != 60003) {
        this.rerunDateOnly = false;
        this.reRunForm.get("reRunDateForm").patchValue(dateF.substr(0, 10));
      } else {
        this.rerunDateOnly = true;
        let startDate = moment(dateF, "DD/MM/YYYY").subtract(1, 'days');
        this.reRunForm.get("reRunDateForm").patchValue(moment(startDate, "DD/MM/YYYY"));
        this.reRunForm.get("reRunDateTo").patchValue(dateF.substr(0, 10));
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
        }
      }, modalConf);
    }
  }

  closeModal() {
    this.modalreRun.closeModal();
  }

  clearSearchData() {
    let now = EnDateToThDate(moment().format('DD/MM/YYYY'));
    this.serchForm.setValue({ dateFrom: now, dateTo: now, batchType: '', operationType: '' });
    this.batchMonitorDT.clear();
    this.statusDropDown.clear();
    this.typeDropDown.clear();
  }

  continueRerun() {
    let startD = this.reRunForm.get("reRunDateForm").value;
    let endD = this.reRunForm.get("reRunDateTo").value;
    this.tempItem.startDate = ThDateToEnDate(startD);
    if (endD != null) {
      this.tempItem.stopDate = ThDateToEnDate(endD);
    }
    this.callRerunJobAPI(this.tempItem)
  }

  callRerunJobAPI(itme) {
    const req = {
      startDate: itme.startDate.substr(0, 10),
      stopDate: itme.stopDate,
      jobtypeCode: itme.jobtypeCode
    }
    this.service.callRerunJobService(req).subscribe(res => {
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
    // console.log("modal event : ", event);
    if (this.rerunDateOnly) {
      this.calendarRerunFrom.refresh();
      this.calendarRerunTo.refresh();
    } else {
      this.calendarRerunFrom.refresh();
    }

  }

  get dateFrom() {
    return this.serchForm.get("dateFrom");
  }
  get dateTo() {
    return this.serchForm.get("dateTo");
  }

  get reRunDateForm() {
    return this.reRunForm.get("reRunDateForm");
  }
  get reRunDateTo() {
    return this.reRunForm.get("reRunDateTo");
  }
  get btnrerun() {
    return this.commonserice.isAuth(PAGE_AUTH.P0001101)
  }

  get operationType() {
    return this.serchForm.get("operationType");
  }
  get batchType() {
    return this.serchForm.get("batchType");
  }

}

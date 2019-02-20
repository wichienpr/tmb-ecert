import { Component, OnInit, ViewChild } from '@angular/core';
import { Sup03000Service } from 'app/tmb-ecert/sup00000/sup03000/sup03000.service';
import { Lov } from 'app/baiwa/common/models';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { Store } from '@ngrx/store';
import * as SUP03000ACTION from "app/tmb-ecert/sup00000/sup03000/sup03000.action";
import * as SUP03000Reducer from "app/tmb-ecert/sup00000/sup03000/sup03000.reducer";
import { sup03000 } from 'app/tmb-ecert/sup00000/sup03000/sup03000.model';
import { Observable } from 'rxjs';
import { CommonService } from 'app/baiwa/common/services';
import { PAGE_AUTH } from 'app/baiwa/common/constants';
import { UserDetail } from 'app/user.model';
import { DatatableDirective, DatatableCofnig } from 'app/baiwa/common/directives/datatable/datatable.directive';
import { DropdownComponent } from 'app/baiwa/common/components/dropdown/dropdown.component';

@Component({
  selector: 'app-sup03000',
  templateUrl: './sup03000.component.html',
  styleUrls: ['./sup03000.component.css']
})
export class Sup03000Component implements OnInit {

  @ViewChild("emailConfigDT")
  emailConfigDT: DatatableDirective;

  @ViewChild("statusDropDown")
  statusDropDown: DropdownComponent;

  isShowResult: Boolean = false;
  dropDownStatus: any;
  form: FormGroup;
  responseObj: any;
  nodataResult: boolean;
  pemailName: any;
  pemailStatus: any;
  storeEmail: Observable<sup03000>;
  sup03000: sup03000;
  user: UserDetail;

  emailDTConfig: DatatableCofnig;

  constructor(private store: Store<AppState>, private service: Sup03000Service,
    private router: Router, private route: ActivatedRoute, private commonService: CommonService) {

    this.form = new FormGroup({
      name: new FormControl(null, Validators.required),
      status: new FormControl('90001', Validators.required)
    });

    this.storeEmail = this.store.select(state => state.sup00000.sup03000);
    this.storeEmail.subscribe(data => {
      this.sup03000 = data;
    });

    this.store.select('user').subscribe(user => {
      this.user = user;
    });


    this.dropDownStatus = {
      dropdownId: "statusType",
      dropdownName: "statusType",
      type: "search",
      formGroup: this.form,
      formControlName: "status",
      values: [],
      valueName: "code",
      labelName: "name"
    }
    this.service.getStatusType().subscribe((obj: Lov[]) => {
      this.dropDownStatus.values = obj
      // console.log("drop down ", obj);
    });

    this.responseObj = [];
    this.nodataResult = false;


  }

  ngOnInit() {

    this.emailDTConfig = {
      url: "/api/setup/sup03000/getEmailTemplate",
      serverSide: true,
      useBlockUi: true
    };
  }

  ngAfterViewInit(): void {
    if (this.sup03000.stateSearch == true) {
      this.isShowResult = true;
      this.form.setValue({ name: this.sup03000.name, status: this.sup03000.status });

      setTimeout(()=>{   
      this.clickSearch();
      }, 500);

    }
  }

  get status() {
    return this.form.get("status");
  }
  get name() {
    return this.form.get("name");
  }

  clickSearch() {

    this.emailConfigDT.searchParams(this.form.value);
    this.emailConfigDT.search();

    this.store.dispatch(new SUP03000ACTION.ClearEmail());
    this.isShowResult = true;

    // console.log("form data ", this.form.value.emailName, " - ", this.form.value.status)
    // this.service.callSearchAPI(this.form).subscribe(res => {
    //   this.responseObj = res;
    //   console.log("res =>", this.responseObj)
    //   if (this.responseObj.length > 0) {
    //     this.nodataResult = false;
    //   } else {
    //     this.nodataResult = true;
    //   }
    // }, error => {

    // },() =>{
    //   this.store.dispatch(new SUP03000ACTION.ClearEmail());
    //   this.isShowResult = true;
    // });

  }

  clickClear() {
    this.store.dispatch(new SUP03000ACTION.ClearEmail());
    this.form.reset({ name: '', status: '' });
    this.statusDropDown.clear();
    this.emailConfigDT.clear();
    // console.log("clear")

  }

  clickEditEmailTemplate(item) {
    const emailState: sup03000 = {
      emailConfigId: item.emailConfig_id,
      emailNameTemplate: item.name,
      emailStatus: item.status,
      name: this.form.value.name,
      status: this.form.value.status,
      stateSearch: true
    }

    this.store.dispatch(new SUP03000ACTION.UpdateEmail(emailState));
    this.router.navigate(["/sup/sup03100"], {});
  }

  get isCanEdit() {
    return this.commonService.ishasAuth(this.user, PAGE_AUTH.P0001501)
  }

}

class AppState {
  sup00000: {
    "sup03000": sup03000
  }
}

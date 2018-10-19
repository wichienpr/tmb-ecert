import { Component, OnInit, AfterViewInit } from '@angular/core';
import { Sup02000Service } from 'app/tmb-ecert/sup00000/sup02000/sup02000.service';
import { FormControl, FormGroup, FormBuilder, FormArray, Validators } from '@angular/forms';
import { Sup02000 } from 'app/tmb-ecert/sup00000/sup02000/sup02000.model';
import { ModalService, CommonService } from 'app/baiwa/common/services';
import { Modal } from 'app/baiwa/common/models';
import { UserDetail } from 'app/user.model';
import { Store } from '@ngrx/store';
import { PAGE_AUTH, MESSAGE_STATUS } from 'app/baiwa/common/constants';

@Component({
  selector: 'app-sup02000',
  templateUrl: './sup02000.component.html',
  styleUrls: ['./sup02000.component.css']
})
export class Sup02000Component implements OnInit, AfterViewInit {

  paramResult: any;
  parameterForm = this.fb.group({
    formPropArray: this.fb.array([])
  });
  responseObj: any;
  user: UserDetail;

  constructor(private store: Store<AppState>, private service: Sup02000Service, private fb: FormBuilder
    , private modal: ModalService, private commonService: CommonService) {

    this.paramResult = [{
      parameterconfigId: "",
      propertyName: "",
      propertyValue: ""

    }];

    this.responseObj = {
      data: "",
      message: ""
    }
    this.store.select('user').subscribe(user => {
      this.user = user;
    });

  }

  ngOnInit() {

  }

  ngAfterViewInit() {
    this.callSearchAPI();
  }

  callSearchAPI() {
    this.service.callGetParamAPI().subscribe(src => {
      this.paramResult = src;
      // console.log("getparam sucess ", src);
      let i = 0;
      this.paramResult.forEach(item => {

        let name = item.propertyName
        let value = item.propertyValue
        let id = item.parameterconfigId

        const newFormGroup: FormGroup = this.fb.group(
          {
            parameterconfigId: new FormControl(id),
            propertyName: new FormControl(name),
            propertyValue: new FormControl(value)
          }
        );
        this.formPropArray.push(newFormGroup);
        i++;
      });
    })
    console.log("getparam sucess ", this.parameterForm);
  }

  clickSave() {
    let listParameter = [];

    let listFormProp: any = this.parameterForm.get('formPropArray');
    listFormProp.controls.forEach(element => {
      if (!(element.pristine)) {
        let obj = {
          parameterconfigId: element.controls.parameterconfigId.value,
          propertyName: element.controls.propertyName.value,
          propertyValue: element.controls.propertyValue.value
        }
        listParameter.push(obj)
      }
    });

    const modalConf: Modal = {
      msg: `<label>ท่านต้องการบันทึกข้อมูลหรือไม่</label>`,
      title: "ยืนยันการบันทึก",
      color: "notification"
    }
    this.modal.confirm((e) => {
      if (e) {
        this.service.callSaveParameterAPI(listParameter).subscribe(res => {
          this.responseObj = res;
          if (this.responseObj.message == MESSAGE_STATUS.FAILED) {
            this.modal.alert({ msg: "ทำรายการล้มเหลว" })
          } else {
            this.modal.alert({ msg: "ทำรายการสำเร็จ" });
            this.callSearchAPI();
          }
        }, err => {
          this.modal.alert({ msg: "ทำรายการล้มเหลว" })
        });
      }
    }, modalConf);
  }
  get formPropArray(): FormArray {
    return this.parameterForm.get('formPropArray') as FormArray;
  }

  get listformPropArray(): FormArray {
    return this.parameterForm.get('formPropArray.controls') as FormArray;
  }

  get isCanSave() {
    return this.commonService.ishasAuth(this.user, PAGE_AUTH.P0001401)
  }


}
interface AppState {
  user: UserDetail
}
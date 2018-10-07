import { Component, OnInit } from '@angular/core';
import { Sup02000Service } from 'app/tmb-ecert/sup00000/sup02000/sup02000.service';
import { FormControl, FormGroup, FormBuilder, FormArray, Validators } from '@angular/forms';
import { Sup02000 } from 'app/tmb-ecert/sup00000/sup02000/sup02000.model';
import { ModalService } from 'app/baiwa/common/services';

@Component({
  selector: 'app-sup02000',
  templateUrl: './sup02000.component.html',
  styleUrls: ['./sup02000.component.css']
})
export class Sup02000Component implements OnInit {

  paramResult: any;
  parameterForm = this.fb.group({
    formPropArray: this.fb.array([])
  });
  responseObj: any;

  constructor(private service: Sup02000Service, private fb: FormBuilder,private modal: ModalService) {
    this.paramResult = [{
      parameterconfigId: "",
      propertyName: "",
      propertyValue: ""

    }];
    
    this.responseObj = {
      data: "",
      message: ""
    }
  }

  ngOnInit() {
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

    this.service.callSaveParameterAPI(listParameter).subscribe(res =>{
      this.responseObj = res;
      if (this.responseObj.message == null) {
        this.modal.alert({ msg: "ทำรายการล้มเหลว" })
      } else {
        this.modal.alert({ msg: this.responseObj.message });
      }
    },err =>{

    });

    // listParameter.forEach(item => {
    //   console.log("value param change ", item.parameterconfigId);
    // });
  }
  get formPropArray(): FormArray {
    return this.parameterForm.get('formPropArray') as FormArray;
  }

  get listformPropArray(): FormArray {
    return this.parameterForm.get('formPropArray.controls') as FormArray;
  }

}

import { Component, OnInit } from '@angular/core';
import { Sup02000Service } from 'app/tmb-ecert/sup00000/sup02000/sup02000.service';
import { FormControl, FormGroup, FormBuilder, FormArray } from '@angular/forms';

@Component({
  selector: 'app-sup02000',
  templateUrl: './sup02000.component.html',
  styleUrls: ['./sup02000.component.css']
})
export class Sup02000Component implements OnInit {

  paramResult: any;

  parameterForm = this.fb.group({
    aliases: this.fb.array([
      this.fb.control('')
    ])
  });

  constructor(private service: Sup02000Service, private fb: FormBuilder) {
    this.paramResult = [{
      parameterconfigId: "",
      propertyName: "",
      propertyValue: ""

    }]
  }

  ngOnInit() {
    this.service.callGetParamAPI().subscribe(src => {
      this.paramResult = src;
      // console.log("getparam sucess ", src);
      // this.paramResult.forEach(item => {

      //   let name = item.propertyName
      //   // item.propertyName = new FormControl(ormControl(item.propertyName);
      //   // this.parameterForm.addControl(name, new FormControl(item.propertyValue))
      //   // this.parameterForm[name] = new FormControl(item.propertyName);

      //   this.aliases.push(this.fb.control(name));
      // });
    })
    console.log("getparam sucess ", this.parameterForm);

  }

  clickSave() {
    this.paramResult.forEach(element => {
      console.log("value param ", element.propertyValue);
    });
  }
  get aliases() {
    return this.parameterForm.get('aliases') as FormArray;
  }

}

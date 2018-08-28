import { Component, OnInit } from '@angular/core';
declare var $: any;
@Component({
  selector: 'app-description',
  templateUrl: './description.component.html',
  styleUrls: ['./description.component.css']
})
export class DescriptionComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }
  popupAddData() {
    $('#modal1').modal('show');
  }

  closePopupAdd() {
    $('#modal1').modal('hide');
  }

}

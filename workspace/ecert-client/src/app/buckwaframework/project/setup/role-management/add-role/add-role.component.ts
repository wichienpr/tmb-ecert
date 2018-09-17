import { Component, OnInit } from '@angular/core';
declare var $: any;
@Component({
  selector: 'app-add-role',
  templateUrl: './add-role.component.html',
  styleUrls: ['./add-role.component.css']
})
export class AddRoleComponent implements OnInit {

  constructor() { }

  ngOnInit() {
    $('.checkbox1 .checkbox').checkbox('attach events' );
  }

}

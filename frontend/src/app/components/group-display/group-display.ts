import {Component, input} from '@angular/core';
import {Group} from '../../model/group';

@Component({
  selector: 'app-group-display',
  imports: [],
  templateUrl: './group-display.html',
  styleUrl: './group-display.css',
})
export class GroupDisplay {
  group = input.required<Group>()
}

import {Component, input} from '@angular/core';

@Component({
  selector: 'app-button',
  imports: [],
  templateUrl: './button.component.html',
  styleUrl: './button.component.css'
})
export class ButtonComponent {

  label = input.required<string>()
  color = input.required<"red" | "purple" | "white">()
}

import {Component, ElementRef, forwardRef, inject, Renderer2, signal} from '@angular/core';
import {ControlValueAccessor, NG_VALUE_ACCESSOR} from '@angular/forms';

@Component({
  selector: 'app-input-field',
  imports: [],
  templateUrl: './input-field.html',
  styleUrl: './input-field.css',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => InputField),
      multi: true
    }
  ]
})
export class InputField implements ControlValueAccessor {
  private _onTouched = () => {}
  private _renderer = inject(Renderer2)
  private _elementRef = inject(ElementRef)
  private _onChange = (_: any) => {}

  protected value = signal<string>("")

  onInput(event: Event) {
    const value = (event.target as HTMLInputElement).value
    console.log(value)
    this.value.set(value)
    this._onChange(value)
    this._onChange(value);
    this._onTouched();
  }

  writeValue(value: string): void {
    this.value.set(value ?? "")
  }

  registerOnChange(fn: (_: any) => void): void {
    this._onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this._onTouched = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    this._renderer.setProperty(this._elementRef.nativeElement, 'disabled', isDisabled);
  }
}

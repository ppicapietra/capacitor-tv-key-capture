# @ppicapietra/capacitor-tv-key-capture

Plugin de Capacitor para capturar eventos de teclas del control remoto en Android TV.

## Características

- Captura eventos de teclas BACK y LEFT ARROW del control remoto
- Solo funciona cuando la aplicación está en primer plano
- API simple con listeners de eventos
- Constantes TypeScript para comparación robusta de teclas

## Instalación

```bash
npm install @ppicapietra/capacitor-tv-key-capture
npx cap sync
```

## Uso

### Importar el plugin

```typescript
import { TvKeyCapture, KeyCodes, KeyNames } from '@ppicapietra/capacitor-tv-key-capture';
```

### Agregar un listener

```typescript
const listener = await TvKeyCapture.addListener('keyPress', (event) => {
  console.log('Tecla presionada:', event.keyName);
  console.log('Código de tecla:', event.keyCode);
  console.log('Timestamp:', event.timestamp);

  // Comparar usando constantes
  if (event.keyName === KeyNames.BACK) {
    console.log('Se presionó BACK');
  } else if (event.keyName === KeyNames.LEFT_ARROW) {
    console.log('Se presionó LEFT ARROW');
  }

  // O comparar usando códigos
  if (event.keyCode === KeyCodes.BACK) {
    console.log('Se presionó BACK');
  } else if (event.keyCode === KeyCodes.LEFT_ARROW) {
    console.log('Se presionó LEFT ARROW');
  }
});
```

### Remover el listener

```typescript
await listener.remove();
```

### Remover todos los listeners

```typescript
await TvKeyCapture.removeAllListeners();
```

## API

### Métodos

#### `addListener(eventName: 'keyPress', listenerFunc: (event: KeyPressEvent) => void): Promise<PluginListenerHandle>`

Agrega un listener para eventos de teclas presionadas.

#### `removeAllListeners(): Promise<void>`

Remueve todos los listeners activos.

### Tipos

#### `KeyPressEvent`

```typescript
interface KeyPressEvent {
  keyCode: number;        // Código de tecla Android (4 para BACK, 21 para LEFT_ARROW)
  keyName: 'BACK' | 'LEFT_ARROW';  // Nombre de la tecla
  timestamp: number;      // Timestamp en milisegundos
}
```

### Constantes

#### `KeyCodes`

```typescript
KeyCodes.BACK = 4
KeyCodes.LEFT_ARROW = 21
```

#### `KeyNames`

```typescript
KeyNames.BACK = 'BACK'
KeyNames.LEFT_ARROW = 'LEFT_ARROW'
```

## Requisitos

- Android API 23 (Android 6.0) o superior
- Android TV
- Capacitor 7.0.0 o superior

## Notas

- El plugin solo captura teclas cuando la aplicación está en primer plano
- Solo se capturan las teclas BACK y LEFT ARROW
- Los eventos de tecla no se consumen, permitiendo que el comportamiento normal continúe

## Desarrollo

### Compilar

```bash
npm run build
```

### Verificar

```bash
npm run verify
```

### Lint

```bash
npm run lint
```

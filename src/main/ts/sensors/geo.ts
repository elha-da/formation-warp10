export type Coordinates = {
  lat: number
  lon: number
}

export function getCurrentPosition(): Promise<Coordinates> {
  return new Promise<Coordinates>((resolve, reject) => {
    navigator.geolocation.getCurrentPosition(
      position => resolve({lat: position.coords.latitude, lon: position.coords.longitude}),
      error => reject(error)
    );
  })
}


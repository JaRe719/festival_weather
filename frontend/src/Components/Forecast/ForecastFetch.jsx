import React, { useEffect, useState } from 'react';

// Helper function to form time ranges
const range = (start, stop, step) =>
  Array.from({ length: (stop - start) / step }, (_, i) => start + i * step);

const fetchWeatherApi = async (url, params) => {
  const response = await fetch(`${url}?${new URLSearchParams(params)}`);
  if (!response.ok) {
    throw new Error('Network response was not ok');
  }
  const data = await response.json();
  return data;
};

const WeatherComponent = () => {
  const [weatherData, setWeatherData] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    const params = {
      latitude: 52.52,
      longitude: 13.41,
      current: [
        'temperature_2m',
        'apparent_temperature',
        'is_day',
        'rain',
        'showers',
        'snowfall',
        'weather_code',
        'cloud_cover',
        'wind_speed_10m',
      ],
      hourly: [
        'temperature_2m',
        'apparent_temperature',
        'rain',
        'showers',
        'snowfall',
        'cloud_cover',
        'wind_speed_10m',
        'wind_direction_10m',
      ],
      daily: [
        'weather_code',
        'temperature_2m_max',
        'sunrise',
        'sunset',
        'daylight_duration',
        'sunshine_duration',
        'uv_index_max',
        'rain_sum',
        'showers_sum',
        'snowfall_sum',
        'wind_speed_10m_max',
        'wind_direction_10m_dominant',
      ],
      timezone: 'auto',
      forecast_days: 16,
    };
    const url = 'https://api.open-meteo.com/v1/forecast';

    const getData = async () => {
      try {
        const response = await fetchWeatherApi(url, params);
        const utcOffsetSeconds = response.utc_offset_seconds || 0;
        const timezone = response.timezone || 'unknown';
        const timezoneAbbreviation = response.timezone_abbreviation || 'unknown';
        const latitude = response.latitude || 0;
        const longitude = response.longitude || 0;

        const current = response.current || {};
        const hourly = response.hourly || {};
        const daily = response.daily || {};

        const weatherData = {
          current: {
            time: new Date((Number(current.time) + utcOffsetSeconds) * 1000),
            temperature2m: current.temperature_2m,
            apparentTemperature: current.apparent_temperature,
            isDay: current.is_day,
            rain: current.rain,
            showers: current.showers,
            snowfall: current.snowfall,
            weatherCode: current.weather_code,
            cloudCover: current.cloud_cover,
            windSpeed10m: current.wind_speed_10m,
          },
          hourly: {
            time: range(Number(hourly.time_start), Number(hourly.time_end), hourly.interval).map(
              (t) => new Date((t + utcOffsetSeconds) * 1000)
            ),
            temperature2m: hourly.temperature_2m,
            apparentTemperature: hourly.apparent_temperature,
            rain: hourly.rain,
            showers: hourly.showers,
            snowfall: hourly.snowfall,
            cloudCover: hourly.cloud_cover,
            windSpeed10m: hourly.wind_speed_10m,
            windDirection10m: hourly.wind_direction_10m,
          },
          daily: {
            time: range(Number(daily.time_start), Number(daily.time_end), daily.interval).map(
              (t) => new Date((t + utcOffsetSeconds) * 1000)
            ),
            weatherCode: daily.weather_code,
            temperature2mMax: daily.temperature_2m_max,
            sunrise: daily.sunrise,
            sunset: daily.sunset,
            daylightDuration: daily.daylight_duration,
            sunshineDuration: daily.sunshine_duration,
            uvIndexMax: daily.uv_index_max,
            rainSum: daily.rain_sum,
            showersSum: daily.showers_sum,
            snowfallSum: daily.snowfall_sum,
            windSpeed10mMax: daily.wind_speed_10m_max,
            windDirection10mDominant: daily.wind_direction_10m_dominant,
          },
        };

        setWeatherData(weatherData);
        console.log(weatherData)
      } catch (error) {
        setError(error.message);
      }
    };

    getData();
  }, []);

  if (error) {
    return <div>Error: {error}</div>;
  }

  return (
    <div>
      {weatherData ? (
        <div>
          <h1>Current Weather</h1>
          <p>Temperature: {weatherData.current.temperature2m}</p>
          {/* Render more current weather details */}
          
          <h1>Hourly Weather</h1>
          {weatherData.hourly.time.map((time, index) => (
            <div key={index}>
              <p>Time: {time.toISOString()}</p>
              <p>Temperature: {weatherData.hourly.temperature2m[index]}</p>
              {/* Render more hourly weather details */}
            </div>
          ))}
          
          <h1>Daily Weather</h1>
          {weatherData.daily.time.map((time, index) => (
            <div key={index}>
              <p>Date: {time.toISOString()}</p>
              <p>Max Temperature: {weatherData.daily.temperature2mMax[index]}</p>
              {/* Render more daily weather details */}
            </div>
          ))}
        </div>
      ) : (
        <p>Loading weather data...</p>
      )}
    </div>
  );
};

export default WeatherComponent;

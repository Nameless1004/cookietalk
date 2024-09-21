/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{js,jsx,ts,tsx}'],
  theme: {
    extend: {
      colors: {
        '--light-gray-0': '#f5f5f5',
        '--light-gray-1': '#eaeaea',
        '--dark-gray-0': '#777777',
        '--dark-gray-1': '#2c2c2c',
        '--primary-orange': '#ff7f00',
        '--secondary-orange': '#FFFAF5',
      },
    },
  },
  plugins: [],
};

import { DateTime } from 'luxon';

function getSecrets() {
    const apiKey = process.env.REACT_APP_API_KEY;
    const apiSecret = process.env.REACT_APP_API_SECRET;

    if (!apiKey || !apiSecret) {
        throw new Error('REACT_APP_API_KEY or REACT_APP_API_SECRET is not defined in the environment variables');
    }

    return [apiKey, apiSecret];
}


async function getStockInfo(stock, barTime) {
    const timeframes = {
        '1D': '1Min',
        '1W': '1H',
        '1M': '1H',
        '3M': '1D',
        'YTD': '1D',
        '1Y': '1D',
        '5Y': '1W'
    };

    const timeframe = timeframes[barTime];
    const [start, end] = getDates(barTime);
    const [apiKey, apiSecret] = getSecrets();

    const url = `https://data.alpaca.markets/v2/stocks/bars?symbols=${stock}&timeframe=${timeframe}&start=${encodeURIComponent(start)}&end=${encodeURIComponent(end)}&limit=1000&adjustment=raw&feed=iex&sort=asc`;
    const options = {
        method: 'GET',
        headers: {
            accept: 'application/json',
            'APCA-API-KEY-ID': apiKey,
            'APCA-API-SECRET-KEY': apiSecret
        }
    };

    try {
        const res = await fetch(url, options);
        const json = await res.json();
        const bars = json['bars'][stock];
        return bars;
    } catch (err) {
        console.error('Fetch error:', err);
        return [];
    }
}

function getDates(time) {
    const end = DateTime.now().toUTC();
    let start;

    switch (time) {
        case '1D':
            start = end.minus({ hours: 24 });
            break;
        case '1W':
            start = end.minus({ days: 7 });
            break;
        case '1M':
            start = end.minus({ months: 1 });
            break;
        case '3M':
            start = end.minus({ months: 3 });
            break;
        case 'YTD':
            start = end.startOf('year');
            break;
        case '1Y':
            start = end.minus({ years: 1 });
            break;
        case '5Y':
            start = end.minus({ years: 5 });
            break;
        default:
            throw new Error('Invalid barTime value');
    }

    const formattedStart = start.toFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    const formattedEnd = end.toFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    return [formattedStart, formattedEnd];
}

export default getStockInfo;

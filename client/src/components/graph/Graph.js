import getStockInfo from "./helper";

function Graph({stock}) {

    const bars = getStockInfo('AAPL', '1D');
    console.log(bars)

    return (<div>
        Hello
    </div>)
}

export default Graph;

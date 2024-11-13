function AddStocks() {
    const handleAddStock = (stockId) => {

    };

    return (<>
        <button
            onClick={() => handleAddStock()}
            type="button"
            className="btn btn-primary my-5"
            style={{ textAlign: 'center', width: '100%' }}
        >
            Add Stock
        </button>
    </>);
}

export default AddStocks;
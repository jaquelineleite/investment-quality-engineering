CREATE TABLE accounts (
    id VARCHAR(50) PRIMARY KEY,
    customer_name VARCHAR(120) NOT NULL,
    currency VARCHAR(10) NOT NULL,
    available_balance NUMERIC(15,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_account_balance
        CHECK (available_balance >= 0)
);


CREATE TABLE orders (
    id VARCHAR(50) PRIMARY KEY,
    client_order_id VARCHAR(100) NOT NULL UNIQUE,
    account_id VARCHAR(50) NOT NULL,
    symbol VARCHAR(20) NOT NULL,
    side VARCHAR(10) NOT NULL,
    quantity NUMERIC(15,4) NOT NULL,
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_orders_account
        FOREIGN KEY (account_id)
        REFERENCES accounts(id),

    CONSTRAINT chk_order_quantity
        CHECK (quantity > 0),

    CONSTRAINT chk_order_side
        CHECK (side IN ('BUY', 'SELL'))
);


CREATE TABLE positions (
    id VARCHAR(50) PRIMARY KEY,
    account_id VARCHAR(50) NOT NULL,
    symbol VARCHAR(20) NOT NULL,
    quantity NUMERIC(15,4) NOT NULL,
    average_price NUMERIC(15,2) NOT NULL,

    CONSTRAINT fk_positions_account
        FOREIGN KEY (account_id)
        REFERENCES accounts(id),

    CONSTRAINT chk_position_quantity
        CHECK (quantity > 0),

    CONSTRAINT chk_average_price
        CHECK (average_price >= 0),

    CONSTRAINT uk_account_symbol
        UNIQUE (account_id, symbol)
);


INSERT INTO accounts (
    id,
    customer_name,
    currency,
    available_balance,
    status
)
VALUES (
    'ACC-001',
    'QA Investment User',
    'USD',
    100000.00,
    'ACTIVE'
);


INSERT INTO orders (
    id,
    client_order_id,
    account_id,
    symbol,
    side,
    quantity,
    status
)
VALUES (
    'ORD-001',
    'CLIENT-ORDER-001',
    'ACC-001',
    'AAPL',
    'BUY',
    2,
    'FILLED'
);


INSERT INTO positions (
    id,
    account_id,
    symbol,
    quantity,
    average_price
)
VALUES (
    'POS-001',
    'ACC-001',
    'AAPL',
    2,
    215.50
);
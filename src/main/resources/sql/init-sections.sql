INSERT INTO storage_section (id, name, attributes_json, unique_keys_json, created_at)
VALUES (
    1,
    'Vehicles',
    '["Brand", "Model", "Year of Manufacture", "VIN", "Registration Number", "Next Technical Check", "Ownership Status", "toll stamp expiry date"]',
    '["VIN", "Registration Number"]',
    NOW()
)
ON CONFLICT (id) DO NOTHING;
<?php

return [
    'oracle' => [
        'driver'         => 'oracle',
        'tns'            => env('DB_OCI_TNS', ''),
        'host'           => env('DB_OCI_HOST', ''),
        'port'           => env('DB_OCI_PORT', ''),
        'database'       => env('DB_OCI_DATABASE', ''),
        'username'       => env('DB_OCI_USERNAME', ''),
        'password'       => env('DB_OCI_PASSWORD', ''),
        'charset'        => env('DB_OCI_CHARSET', ''),
        'prefix'         => env('DB_OCI_PREFIX', ''),
        'prefix_schema'  => env('DB_OCI_SCHEMA_PREFIX', ''),
        'server_version' => env('DB_OCI_SERVER_VERSION', '11g'),
    ],
];

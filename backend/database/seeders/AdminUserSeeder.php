<?php

namespace Database\Seeders;

use App\Models\User;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\Hash;

class AdminUserSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        User::updateOrCreate(
            ['username' => 'admin'],
            [
                'name' => 'Campus Admin',
                'username' => 'admin',
                'email' => 'admin@smartcampus.local',
                'password' => Hash::make('admin'),
                'role' => 'Admin',
            ]
        );
    }
}

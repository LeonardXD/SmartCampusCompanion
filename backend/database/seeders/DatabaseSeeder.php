<?php

namespace Database\Seeders;

// use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;

class DatabaseSeeder extends Seeder
{
    /**
     * Seed the application's database.
     */
    public function run(): void
    {
        $this->call([
            DepartmentSeeder::class,
        ]);

        DB::table('announcements')->insert([
            [
                'title' => 'Welcome to Smart Campus',
                'content' => 'We are excited to launch the new Smart Campus Companion app!',
                'category' => 'General',
                'is_important' => true,
                'is_read' => false,
                'created_at' => now(),
                'updated_at' => now(),
            ],
            [
                'title' => 'System Maintenance',
                'content' => 'The system will be down for maintenance this Sunday from 2 AM to 4 AM.',
                'category' => 'Maintenance',
                'is_important' => false,
                'is_read' => false,
                'created_at' => now(),
                'updated_at' => now(),
            ],
        ]);
    }
}

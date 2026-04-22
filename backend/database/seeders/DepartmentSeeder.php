<?php

namespace Database\Seeders;

use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;

class DepartmentSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        DB::table('departments')->insert([
            [
                'name' => 'College of Computer Studies',
                'description' => 'CCS focuses on information technology and computer science education.',
                'contact_email' => 'ccs@smartcampus.edu',
                'contact_phone' => '123-456-7890',
            ],
            [
                'name' => 'College of Engineering',
                'description' => 'Engineering department offers various engineering disciplines.',
                'contact_email' => 'engineering@smartcampus.edu',
                'contact_phone' => '123-456-7891',
            ],
            [
                'name' => 'Student Affairs Office',
                'description' => 'SAO handles student-related services and activities.',
                'contact_email' => 'sao@smartcampus.edu',
                'contact_phone' => '123-456-7892',
            ],
        ]);
    }
}

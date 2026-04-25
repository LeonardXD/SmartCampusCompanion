<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Department;

class DepartmentController extends Controller
{
    public function index()
    {
        if (!Department::query()->exists()) {
            Department::query()->insert([
                [
                    'name' => 'College of Computer Studies',
                    'description' => 'CCS focuses on information technology and computer science education.',
                    'contact_email' => 'ccs@smartcampus.edu',
                    'contact_phone' => '123-456-7890',
                    'created_at' => now(),
                    'updated_at' => now(),
                ],
                [
                    'name' => 'College of Engineering',
                    'description' => 'Engineering department offers various engineering disciplines.',
                    'contact_email' => 'engineering@smartcampus.edu',
                    'contact_phone' => '123-456-7891',
                    'created_at' => now(),
                    'updated_at' => now(),
                ],
                [
                    'name' => 'Student Affairs Office',
                    'description' => 'SAO handles student-related services and activities.',
                    'contact_email' => 'sao@smartcampus.edu',
                    'contact_phone' => '123-456-7892',
                    'created_at' => now(),
                    'updated_at' => now(),
                ],
            ]);
        }

        return response()->json(
            Department::query()->orderBy('name')->get()
        );
    }
}

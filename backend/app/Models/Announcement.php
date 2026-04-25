<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Announcement extends Model
{
    use HasFactory;

    protected $fillable = [
        'title',
        'content',
        'category',
        'is_important',
        'is_read',
    ];

    protected $casts = [
        'is_important' => 'boolean',
        'is_read' => 'boolean',
    ];
}

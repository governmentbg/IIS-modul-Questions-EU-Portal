<?php

namespace App\Models;

use App\Models\MNews;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $M_NewsMG_id
 * @property int        $M_News_id
 * @property int        $M_NIT_id
 * @property string     $M_NewsMG_file
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class MNewsImg extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'M_News_Img';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'M_NewsMG_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'M_News_id', 'M_NIT_id', 'M_NewsMG_file'
    ];

    /**
     * The attributes excluded from the model's JSON form.
     *
     * @var array
     */
    protected $hidden = [];

    /**
     * The attributes that should be casted to native types.
     *
     * @var array
     */
    protected $casts = [
        'M_NewsMG_id' => 'int', 'M_News_id' => 'int', 'M_NIT_id' => 'int', 'M_NewsMG_file' => 'string', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
    ];

    /**
     * The attributes that should be mutated to dates.
     *
     * @var array
     */
    protected $dates = [
        'created_at', 'updated_at', 'deleted_at'
    ];

    /**
     * Indicates if the model should be timestamped.
     *
     * @var boolean
     */
    public $timestamps = false;

    // Scopes...

    // Functions ...

    // Relations ...
    public function eq_news()
    {
        return $this->belongsTo(MNews::class, 'M_News_id');
    }
}
